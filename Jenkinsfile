@Library('utils@master')_

def utils = new hee.tis.utils()

node {

    def service = "profile"
    def containerRegistryLocaltion = "430723991443.dkr.ecr.eu-west-2.amazonaws.com"

    deleteDir()

    stage('Checkout Git Repo') {
      checkout scm
    }

    env.GIT_COMMIT=sh(returnStdout: true, script: "git log -n 1 --pretty=format:'%h'").trim()
    def mvn = "${tool 'Maven 3.3.9'}/bin/mvn"
    def workspace = pwd()
    def parent_workspace = pwd()
    def repository = "${env.GIT_COMMIT}".split("TIS-")[-1].split(".git")[0]
    def buildNumber = env.BUILD_NUMBER
    def buildVersion = env.GIT_COMMIT
    def imageName = ""
    def imageVersionTag = ""

    println "[Jenkinsfile INFO] Commit Hash is ${GIT_COMMIT}"

    try {

        milestone 1


        stage('Build') {
          sh "'${mvn}' clean install -DskipTests"
        }

        stage('Unit Tests') {
          try {
            sh "'${mvn}' clean test"
          } finally {
            junit '**/target/surefire-reports/TEST-*.xml'
          }
        }

        stage('Analyze Quality') {
          withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
            if (env.CHANGE_ID) {
              sh "'${mvn}' sonar:sonar -Dsonar.login='${SONAR_TOKEN}' -Dsonar.pullrequest.key=$env.CHANGE_ID"
            } else {
              sh "'${mvn}' sonar:sonar -Dsonar.login='${SONAR_TOKEN}' -Dsonar.branch.name=$env.BRANCH_NAME"
            }
          }
        }

        milestone 2

        stage('Dockerise') {
          env.VERSION = utils.getMvnToPom(workspace, 'version')
          env.GROUP_ID = utils.getMvnToPom(workspace, 'groupId')
          env.ARTIFACT_ID = utils.getMvnToPom(workspace, 'artifactId')
          env.PACKAGING = utils.getMvnToPom(workspace, 'packaging')
          imageName = env.ARTIFACT_ID
          imageVersionTag = env.GIT_COMMIT

          if (isService) {
              imageName = service
              env.IMAGE_NAME = imageName
          }

          //urghhh
          sh "mvn package -DskipTests"
          sh "cp ./profile-service/target/profile-service-*.war ./profile-service/target/app.jar"

          def dockerImageName = "profile"
          def containerRegistryLocaltion = "430723991443.dkr.ecr.eu-west-2.amazonaws.com"

          // log into aws docker
          sh "aws ecr get-login-password --region eu-west-2 | docker login --username AWS --password-stdin 430723991443.dkr.ecr.eu-west-2.amazonaws.com"

          sh "docker build -t ${containerRegistryLocaltion}/${dockerImageName}:$buildVersion -f ./profile-service/Dockerfile ./profile-service"
          sh "docker push ${containerRegistryLocaltion}/${dockerImageName}:$buildVersion"

          sh "docker tag ${containerRegistryLocaltion}/${dockerImageName}:$buildVersion ${containerRegistryLocaltion}/tcs:latest"
          sh "docker push ${containerRegistryLocaltion}/${dockerImageName}:latest"

          sh "docker rmi ${containerRegistryLocaltion}/${dockerImageName}:latest"
          sh "docker rmi ${containerRegistryLocaltion}/${dockerImageName}:$buildVersion"

          println "[Jenkinsfile INFO] Stage Dockerize completed..."
        }

    } catch (err) {

        throw err

    } finally {

        if (env.BRANCH_NAME == "master") {

            milestone 3

            stage('Staging') {
              node {
                println "[Jenkinsfile INFO] Stage Deploy starting..."

                sh "ansible-playbook -i $env.DEVOPS_BASE/ansible/inventory/stage $env.DEVOPS_BASE/ansible/${service}.yml --extra-vars=\"{\'versions\': {\'${service}\': \'${env.GIT_COMMIT}\'}}\""
              }
            }

            milestone 4

            stage('Approval') {
              timeout(time:5, unit:'HOURS') {
                input message: 'Deploy to production?', ok: 'Deploy!'
              }
            }

            milestone 5

            stage('Production') {
              node {
                sh "ansible-playbook -i $env.DEVOPS_BASE/ansible/inventory/prod $env.DEVOPS_BASE/ansible/${service}.yml --extra-vars=\"{\'versions\': {\'${service}\': \'${env.GIT_COMMIT}\'}}\""
              }
            }

        }

    }
}

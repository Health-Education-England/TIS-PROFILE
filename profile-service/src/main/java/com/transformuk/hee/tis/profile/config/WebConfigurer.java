package com.transformuk.hee.tis.profile.config;

import io.undertow.UndertowOptions;
import java.io.File;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.MimeMappings;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration of web application with Servlet 3.0 APIs.
 */
@Configuration
public class WebConfigurer implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

  private final Logger log = LoggerFactory.getLogger(WebConfigurer.class);

  /**
   * Customize the Servlet engine: Mime types, the document root, the cache.
   */
  @Override
  public void customize(ConfigurableServletWebServerFactory webServerFactory) {
    MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
    // IE issue, see https://github.com/jhipster/generator-jhipster/pull/711
    mappings.add("html", "text/html;charset=utf-8");
    // CloudFoundry issue, see https://github.com/cloudfoundry/gorouter/issues/64
    mappings.add("json", "text/html;charset=utf-8");
    webServerFactory.setMimeMappings(mappings);
    // When running in an IDE or with ./mvnw spring-boot:run, set location of the static web assets.
    setLocationForStaticAssets(webServerFactory);

    /*
     * Enable HTTP/2 for Undertow - https://twitter.com/ankinson/status/829256167700492288
     * HTTP/2 requires HTTPS, so HTTP requests will fallback to HTTP/1.1.
     */
    if (webServerFactory instanceof UndertowServletWebServerFactory) {

      ((UndertowServletWebServerFactory) webServerFactory)
              .addBuilderCustomizers(builder ->
                      builder.setServerOption(UndertowOptions.ENABLE_HTTP2, true));
    }
  }

  private void setLocationForStaticAssets(ConfigurableServletWebServerFactory webServerFactory) {
    File root;
    String prefixPath = resolvePathPrefix();
    root = new File(prefixPath + "ui-build/profile/");
    if (root.exists() && root.isDirectory()) {
      webServerFactory.setDocumentRoot(root);
    }
  }

  /**
   * Resolve path prefix to static resources.
   */
  private String resolvePathPrefix() {
    String fullExecutablePath = this.getClass().getResource("").getPath();
    String rootPath = Paths.get(".").toUri().normalize().getPath();
    String extractedPath = fullExecutablePath.replace(rootPath, "");
    int extractionEndIndex = extractedPath.indexOf("target/");
    if (extractionEndIndex <= 0) {
      return "";
    }
    return extractedPath.substring(0, extractionEndIndex);
  }
}

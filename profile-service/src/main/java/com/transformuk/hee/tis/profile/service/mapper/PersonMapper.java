package com.transformuk.hee.tis.profile.service.mapper;

import com.transformuk.hee.tis.profile.domain.Person;
import com.transformuk.hee.tis.profile.service.dto.PersonDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity Person and its DTO PersonDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PersonMapper {

  PersonDTO personToPersonDTO(Person person);

  List<PersonDTO> peopleToPersonDTOs(List<Person> people);

  Person personDTOToPerson(PersonDTO personDTO);

  List<Person> personDTOsToPeople(List<PersonDTO> personDTOs);
}

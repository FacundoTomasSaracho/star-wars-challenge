package org.facundosaracho.starwarschallenge.mapper;

import org.facundosaracho.starwarschallenge.model.dto.PaginatedPeopleResponseDto;
import org.facundosaracho.starwarschallenge.model.domain.PeopleResponse;
import org.facundosaracho.starwarschallenge.model.domain.Result;
import org.facundosaracho.starwarschallenge.model.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PeopleMapper {

    // Dejalo si todavía tenés llamadas estáticas en algún lado
    PeopleMapper INSTANCE = Mappers.getMapper(PeopleMapper.class);

    // ===================== BY ID =====================

    @Mapping(target = "results", source = ".", qualifiedByName = "singleResultToList")
    @Mapping(target = "message", source = "message")
    PeopleResponse mapByIdResponseToPeopleResponse(SwapiPeopleByIdResponseDto byIdResponse);

    @Mapping(target = "_id", source = "id")
    @Mapping(target = "uid", source = "uid")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "__v", source = "v")
    @Mapping(target = "properties", source = "properties")
    Result mapPeopleResultToResult(SwapiPeopleByIdResponseDto.PeopleResult src);

    // DTO properties(byId) -> dominio properties
    @Mapping(target = "birth_year", source = "birthYear")
    @Mapping(target = "eye_color",  source = "eyeColor")
    @Mapping(target = "hair_color", source = "hairColor")
    @Mapping(target = "skin_color", source = "skinColor")
    org.facundosaracho.starwarschallenge.model.domain.Properties
    mapPeoplePropertiesToDomain(SwapiPeopleByIdResponseDto.PeopleProperties src);

    @Named("singleResultToList")
    default List<Result> singleResultToList(SwapiPeopleByIdResponseDto response) {
        if (response == null || response.result() == null) return Collections.emptyList();
        return List.of(mapPeopleResultToResult(response.result()));
    }

    // ===================== Helpers para PeopleResponse -> PaginatedPeopleDto =====================

    @Named("mapResultListToPaginatedPersonDtoList")
    default List<PaginatedPeopleDto.PaginatedPersonDto> mapResultListToPaginatedPersonDtoListNamed(List<Result> results) {
        if (results == null) return Collections.emptyList();
        return results.stream()
                .map(this::mapResultToPaginatedPersonDto)
                .toList();
    }

    @Named("mapToPaginationInfo")
    default PaginatedPeopleDto.PaginationInformationDto mapToPaginationInfo(PeopleResponse peopleResponse) {
        if (peopleResponse == null) {
            return new PaginatedPeopleDto.PaginationInformationDto(null, 0L, 0L, null, null);
        }
        return new PaginatedPeopleDto.PaginationInformationDto(
                peopleResponse.message(),
                peopleResponse.totalRecords(),
                peopleResponse.totalPages(),
                peopleResponse.previous(),
                peopleResponse.next()
        );
    }

    @Mapping(target = "uid", source = "uid")
    @Mapping(target = "name", source = "properties.name")
    @Mapping(target = "url", source = "properties.url")
    PaginatedPeopleDto.PaginatedPersonDto mapResultToPaginatedPersonDto(Result result);

    @Mapping(target = "personDto", source = "results", qualifiedByName = "mapResultListToPaginatedPersonDtoList")
    @Mapping(target = "pageInformation", source = ".", qualifiedByName = "mapToPaginationInfo")
    PaginatedPeopleDto mapPeopleResponseToPaginatedPeopleDto(PeopleResponse peopleResponse);

    // ===================== BY NAME =====================
    // Ahora MapStruct sabe convertir cada elemento 'Result' del DTO al dominio.Result
    @Mapping(target = "results", source = "result")
    @Mapping(target = "message", source = "message")
    PeopleResponse mapByNameResponseToPeopleResponse(SwapiPeopleByNameResponseDto byNameResponse);

    // Elemento: DTO(byName).Result -> dominio.Result
    @Mapping(target = "_id", source = "id")
    @Mapping(target = "uid", source = "uid")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "__v", source = "v")
    @Mapping(target = "properties", source = "properties")
    Result mapByNameResultToResult(SwapiPeopleByNameResponseDto.Result src);

    // DTO properties(byName) -> dominio properties
    @Mapping(target = "birth_year", source = "birth_year")
    @Mapping(target = "eye_color",  source = "eyeColor")
    @Mapping(target = "hair_color", source = "hairColor")
    @Mapping(target = "skin_color", source = "skinColor")
    org.facundosaracho.starwarschallenge.model.domain.Properties
    mapByNamePropertiesToDomain(SwapiPeopleByNameResponseDto.Properties src);

    // ===================== PeopleResponse -> PeopleDTO =====================

    @Mapping(target = "people", source = "results")
    @Mapping(target = "message", source = "message")
    @Mapping(target = "totalCount", source = "results", qualifiedByName = "calculateTotalCount")
    PeopleDTO mapPeopleResponseToPeopleDTO(PeopleResponse peopleResponse);

    @Named("calculateTotalCount")
    default int calculateTotalCount(List<Result> results) {
        return results != null ? results.size() : 0;
    }

    // Result (dominio) -> PersonDto
    @Mapping(target = "id", source = "_id")
    @Mapping(target = "uid", source = "uid")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "version", source = "__v")
    @Mapping(target = "name", source = "properties.name")
    @Mapping(target = "gender", source = "properties.gender")
    @Mapping(target = "height", source = "properties.height")
    @Mapping(target = "mass", source = "properties.mass")
    @Mapping(target = "hairColor", source = "properties.hair_color")
    @Mapping(target = "eyeColor", source = "properties.eye_color")
    @Mapping(target = "skinColor", source = "properties.skin_color")
    @Mapping(target = "birthYear", source = "properties.birth_year")
    @Mapping(target = "homeworld", source = "properties.homeworld")
    @Mapping(target = "created", source = "properties.created")
    @Mapping(target = "edited", source = "properties.edited")
    @Mapping(target = "url", source = "properties.url")
    @Mapping(target = "vehicles", source = "properties.vehicles")
    @Mapping(target = "starships", source = "properties.starships")
    @Mapping(target = "films", source = "properties.films")
    PersonDto mapResultToPersonDTO(Result result);

    List<PersonDto> mapResultListToPersonDTOList(List<Result> results);

    // ===================== PAGINATED (items livianos) =====================

    // Item liviano -> PersonDto (se mantiene)
    @Mapping(target = "uid", source = "uid")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "url", source = "url")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "height", ignore = true)
    @Mapping(target = "mass", ignore = true)
    @Mapping(target = "hairColor", ignore = true)
    @Mapping(target = "eyeColor", ignore = true)
    @Mapping(target = "skinColor", ignore = true)
    @Mapping(target = "birthYear", ignore = true)
    @Mapping(target = "homeworld", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "edited", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    @Mapping(target = "starships", ignore = true)
    @Mapping(target = "films", ignore = true)
    PersonDto mapPaginatedResultToPersonDto(PaginatedPeopleResponseDto.Result paginatedResult);

    List<PersonDto> mapPaginatedResultListToPersonDtoList(List<PaginatedPeopleResponseDto.Result> paginatedResults);

    // Item liviano -> dominio.Result con properties mínimas (name/url)
    @Mapping(target = "uid", source = "uid")
    @Mapping(target = "url", source = "url")
    @Mapping(target = "_id", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "__v", ignore = true)
    @Mapping(target = "properties", source = ".", qualifiedByName = "minimalPropsFromPaginated")
    Result mapPaginatedResultToResult(PaginatedPeopleResponseDto.Result paginatedResult);

    @Named("minimalPropsFromPaginated")
    default org.facundosaracho.starwarschallenge.model.domain.Properties
    minimalPropsFromPaginated(PaginatedPeopleResponseDto.Result r) {
        // Ajustá al constructor real de tu 'Properties' de dominio si difiere
        return new org.facundosaracho.starwarschallenge.model.domain.Properties(
                null, // created
                null, // edited
                r.name(),
                null, // gender
                null, // skin_color
                null, // hair_color
                null, // height
                null, // eye_color
                null, // mass
                null, // homeworld
                null, // birth_year
                null, // vehicles
                null, // starships
                null, // films
                r.url()
        );
    }

    List<Result> mapPaginatedResultListToResultList(List<PaginatedPeopleResponseDto.Result> paginatedResults);

    @Mapping(target = "results", source = "results")
    @Mapping(target = "message", source = "message")
    @Mapping(target = "totalRecords", source = "totalRecords")
    @Mapping(target = "totalPages", source = "totalPages")
    @Mapping(target = "previous", source = "previous")
    @Mapping(target = "next", source = "next")
    PeopleResponse mapPaginatedPeopleResponseToPeopleResponse(PaginatedPeopleResponseDto paginatedResponse);

    // ===================== Conveniencia =====================

    default PeopleDTO mapSwapiResponseToPeopleDTO(Object swapiResponse) {
        PeopleResponse peopleResponse = mapToPeopleResponse(swapiResponse);
        return mapPeopleResponseToPeopleDTO(peopleResponse);
    }

    default PeopleResponse mapToPeopleResponse(Object swapiResponse) {
        if (swapiResponse instanceof SwapiPeopleByIdResponseDto byId) {
            return mapByIdResponseToPeopleResponse(byId);
        } else if (swapiResponse instanceof SwapiPeopleByNameResponseDto byName) {
            return mapByNameResponseToPeopleResponse(byName);
        } else if (swapiResponse instanceof PaginatedPeopleResponseDto paginated) {
            return mapPaginatedPeopleResponseToPeopleResponse(paginated);
        }
        throw new IllegalArgumentException("Tipo de respuesta no soportado: " + swapiResponse.getClass().getSimpleName());
    }
}

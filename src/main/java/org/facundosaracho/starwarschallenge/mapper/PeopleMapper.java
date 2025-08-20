package org.facundosaracho.starwarschallenge.mapper;

import org.facundosaracho.starwarschallenge.model.domain.PaginatedPeopleResponse;
import org.facundosaracho.starwarschallenge.model.domain.PeopleResponse;
import org.facundosaracho.starwarschallenge.model.domain.Result;
import org.facundosaracho.starwarschallenge.model.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;

public interface PeopleMapper {

    PeopleMapper INSTANCE = Mappers.getMapper(PeopleMapper.class);

    // ========== MAPEOS EXISTENTES ==========

    @Mapping(target = "results", source = ".", qualifiedByName = "singleResultToList")
    @Mapping(target = "message", source = "message")
    PeopleResponse mapByIdResponseToPeopleResponse(SwapiPeopleByIdResponseDto byIdResponse);

    @Mapping(target = "results", source = "result")
    @Mapping(target = "message", source = "message")
    PeopleResponse mapByNameResponseToPeopleResponse(SwapiPeopleByNameResponseDto byNameResponse);

    @Named("singleResultToList")
    default List<Result> singleResultToList(SwapiPeopleByIdResponseDto response) {
        if (response == null || response.result() == null) {
            return Collections.emptyList();
        }
        return List.of(response.result());
    }

    @Mapping(target = "people", source = "results")
    @Mapping(target = "message", source = "message")
    @Mapping(target = "totalCount", source = "results", qualifiedByName = "calculateTotalCount")
    PeopleDTO mapPeopleResponseToPeopleDTO(PeopleResponse peopleResponse);

    // MAPEO PARA Result (clase independiente) -> PersonDto
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

    @Named("calculateTotalCount")
    default int calculateTotalCount(List<Result> results) {
        return results != null ? results.size() : 0;
    }

    // ========== MAPEOS PARA PAGINATEDPEOPLERESPONSE ==========

    // MAPEO DIRECTO: PaginatedPeopleResponse.Result -> PersonDto
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
    PersonDto mapPaginatedResultToPersonDto(PaginatedPeopleResponse.Result paginatedResult);

    List<PersonDto> mapPaginatedResultListToPersonDtoList(List<PaginatedPeopleResponse.Result> paginatedResults);

    // NUEVO MAPEOS: Result -> PaginatedPersonDto
    @Mapping(target = "uid", source = "uid")
    @Mapping(target = "name", source = "properties.name")
    @Mapping(target = "url", source = "url")
    PaginatedPeopleDto.PaginatedPersonDto mapResultToPaginatedPersonDto(Result result);

    @Named("mapResultListToPaginatedPersonDtoList")
    default List<PaginatedPeopleDto.PaginatedPersonDto> mapResultListToPaginatedPersonDtoListNamed(List<Result> results) {
        if (results == null) return Collections.emptyList();
        return results.stream()
                .map(this::mapResultToPaginatedPersonDto)
                .toList();
    }

    @Mapping(target = "personDto", source = "results", qualifiedByName = "mapResultListToPaginatedPersonDtoList")
    @Mapping(target = "pageInformation", source = ".", qualifiedByName = "mapToPaginationInfo")
    PaginatedPeopleDto mapPeopleResponseToPaginatedPeopleDto(PeopleResponse peopleResponse);

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

    // ========== MAPEOS CORREGIDOS PARA PaginatedPeopleResponse -> PeopleResponse ==========

    @Mapping(target = "uid", source = "uid")
    @Mapping(target = "url", source = "url")
    @Mapping(target = "properties.name", source = "name")
    @Mapping(target = "properties.url", source = "url")
    @Mapping(target = "_id", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "__v", ignore = true)
    @Mapping(target = "properties.created", ignore = true)
    @Mapping(target = "properties.edited", ignore = true)
    @Mapping(target = "properties.gender", ignore = true)
    @Mapping(target = "properties.skin_color", ignore = true)
    @Mapping(target = "properties.hair_color", ignore = true)
    @Mapping(target = "properties.height", ignore = true)
    @Mapping(target = "properties.eye_color", ignore = true)
    @Mapping(target = "properties.mass", ignore = true)
    @Mapping(target = "properties.homeworld", ignore = true)
    @Mapping(target = "properties.birth_year", ignore = true)
    @Mapping(target = "properties.vehicles", ignore = true)
    @Mapping(target = "properties.starships", ignore = true)
    @Mapping(target = "properties.films", ignore = true)
    Result mapPaginatedResultToResult(PaginatedPeopleResponse.Result paginatedResult);

    List<Result> mapPaginatedResultListToResultList(List<PaginatedPeopleResponse.Result> paginatedResults);

    @Mapping(target = "results", source = "results")
    @Mapping(target = "message", source = "message")
    @Mapping(target = "totalRecords", source = "total_records")
    @Mapping(target = "totalPages", source = "total_pages")
    @Mapping(target = "previous", source = "previous")
    @Mapping(target = "next", source = "next")
    PeopleResponse mapPaginatedPeopleResponseToPeopleResponse(PaginatedPeopleResponse paginatedResponse);

    // ========== MÉTODOS DE CONVENIENCIA ==========

    default PeopleDTO mapSwapiResponseToPeopleDTO(Object swapiResponse) {
        PeopleResponse peopleResponse = mapToPeopleResponse(swapiResponse);
        return mapPeopleResponseToPeopleDTO(peopleResponse);
    }

    default PeopleResponse mapToPeopleResponse(Object swapiResponse) {
        if (swapiResponse instanceof SwapiPeopleByIdResponseDto) {
            return mapByIdResponseToPeopleResponse((SwapiPeopleByIdResponseDto) swapiResponse);
        } else if (swapiResponse instanceof SwapiPeopleByNameResponseDto) {
            return mapByNameResponseToPeopleResponse((SwapiPeopleByNameResponseDto) swapiResponse);
        } else if (swapiResponse instanceof PaginatedPeopleResponse) {
            return mapPaginatedPeopleResponseToPeopleResponse((PaginatedPeopleResponse) swapiResponse);
        }
        throw new IllegalArgumentException("Tipo de respuesta no soportado: " + swapiResponse.getClass().getSimpleName());
    }
}

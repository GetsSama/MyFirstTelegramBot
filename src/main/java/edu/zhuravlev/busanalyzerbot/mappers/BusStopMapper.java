package edu.zhuravlev.busanalyzerbot.mappers;

import edu.zhuravlev.busanalyzerbot.entities.BusStop;
import edu.zhuravlev.busanalyzerbot.repositories.busstop.BusStopTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface BusStopMapper {
    BusStopMapper INSTANCE = Mappers.getMapper(BusStopMapper.class);
    String splitter = ",\\s*";

    @Mapping(target = "priorityBuses", expression = "java(getListPriorityBusesFromString(busStopTable.getPriorityBuses()))")
    @Mapping(target = "user", ignore = true)
    BusStop toEntity(BusStopTable busStopTable);

    @Mapping(target = "priorityBuses", expression = "java(getStringPriorityBusesFromList(busStop.getPriorityBuses()))")
    @Mapping(target = "user", ignore = true)
    BusStopTable toTable(BusStop busStop);
    @Mapping(target = "priorityBuses", expression = "java(getStringPriorityBusesFromList(updater.getPriorityBuses()))")
    @Mapping(target = "user", ignore = true)
    void updateBusStopTable(@MappingTarget BusStopTable updatable, BusStop updater);

    default Set<String> getListPriorityBusesFromString(String buses) {
            String[] busesNames = buses.split(splitter);
            return Set.of(busesNames);
    }

    default String getStringPriorityBusesFromList(Set<String> buses) {
        String[] busesStr = buses.toArray(new String[0]);
        Arrays.sort(busesStr);
        StringBuilder stringBuilder = new StringBuilder();
        var busIter = Arrays.stream(busesStr).iterator();

        while (busIter.hasNext()) {
            stringBuilder.append(busIter.next());
            if(busIter.hasNext())
                stringBuilder.append(",");
        }

        return stringBuilder.toString();
    }
}

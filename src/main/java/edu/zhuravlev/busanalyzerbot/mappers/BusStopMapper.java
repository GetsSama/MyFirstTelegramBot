package edu.zhuravlev.busanalyzerbot.mappers;

import edu.zhuravlev.busanalyzerbot.entities.BusStop;
import edu.zhuravlev.busanalyzerbot.repositories.busstop.BusStopTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Iterator;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {BusStopMapper.class})
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
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<String> busIter = buses.iterator();

        while (busIter.hasNext()) {
            stringBuilder.append(busIter.next());
            if(busIter.hasNext())
                stringBuilder.append(",");
        }

        return stringBuilder.toString();
    }
}

package edu.zhuravlev.busanalyzerbot.mappers;

import edu.zhuravlev.busanalyzerbot.entities.BusStop;
import edu.zhuravlev.busanalyzerbot.repositories.busstop.BusStopTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

@Mapper(componentModel = "spring")
public interface BusStopMapper {
    final String spliter = ",\\s*";

    @Mapping(target = "priorityBuses", expression = "java(getListPriorityBusesFromString(busStopTable.getPriorityBuses()))")
    BusStop toEntity(BusStopTable busStopTable);

    @Mapping(target = "priorityBuses", expression = "java(getStringPriorityBusesFromList(busStop.getPriorityBuses()))")
    BusStopTable toTable(BusStop busStop);

    default List<String> getListPriorityBusesFromString(String buses) {
            String[] busesNames = buses.split(spliter);
            return List.of(busesNames);
    }

    default String getStringPriorityBusesFromList(List<String> buses) {
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

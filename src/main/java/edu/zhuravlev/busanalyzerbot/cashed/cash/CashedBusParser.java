package edu.zhuravlev.busanalyzerbot.cashed.cash;

import busentity.Bus;
import busparser.BusParser;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Slf4j
public class CashedBusParser implements BusParser {
    private final BusParser realParser;
    private final String busIdRegex = "stop__\\d{7}";
    private final Pattern busIdPattern;
    private final Map<String, CashedData> cash = new ConcurrentHashMap<>();

    public CashedBusParser(BusParser busParser) {
        this.realParser = busParser;
        this.busIdPattern = Pattern.compile(busIdRegex);
    }

    @Override
    public List<Bus> parse(String s) {
        var busId = getBusId(s);
        var dataFromCash = cash.get(busId);
        if(dataFromCash != null) {
            var now = LocalDateTime.now();
            if(dataFromCash.lastModified().plusMinutes(1).isAfter(now)) {
                log.info("Parser return data for bus stop: " + busId + " from cash");
                return dataFromCash.getData();
            }
            else {
                var newData = realParser.parse(s);
                dataFromCash.updateData(newData);
                log.info("Parser return data for bus stop: " + busId + " from Maps response with cash update");
                return newData;
            }
        } else {
            var data = realParser.parse(s);
            cash.put(busId, new CashedData(data));
            log.info("Parser return data for bus stop: " + busId + " from Maps response");
            return data;
        }
    }

    @Override
    public List<Bus> parse(File file) {
        return realParser.parse(file);
    }

    private String getBusId(String url) {
        var afterSplit = url.split("/");
        for(var str : afterSplit) {
            if(busIdPattern.matcher(str).matches())
                return str;
        }
        throw new IllegalArgumentException("Given URL: " + url + " not Maps bus stop station URL!");
    }

    private static class CashedData {
        private LocalDateTime creationTime;
        private List<Bus> data;

        public CashedData(List<Bus> data) {
            this.creationTime = LocalDateTime.now();
            this.data = data;
        }

        public synchronized LocalDateTime lastModified() {
            return creationTime;
        }

        public synchronized List<Bus> getData() {
            return data;
        }

        public synchronized void updateData(List<Bus> data) {
            this.data = data;
            this.creationTime = LocalDateTime.now();
        }
    }
}

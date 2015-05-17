package cn.edu.scnu.pmtreewebapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import cn.edu.scnu.pmtreewebapp.model.RoadNetworkGraphs;

@SpringBootApplication
public class PmtreewebappApplication {
    public static void main(String[] args) {
        RoadNetworkGraphs.loadRoadNetworkGraphs();
        SpringApplication.run(PmtreewebappApplication.class, args);
    }
}

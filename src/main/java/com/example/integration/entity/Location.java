package com.example.integration.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Location {

    @Id
    private String id;
    private String name;
}


/*
 {
                "id": "urn:li:geo:100907646",
                "name": "Stockholm, Stockholm County, Sweden"
            },
            {
                "id": "urn:li:geo:104853962",
                "name": "Stockholm County, Sweden"
            },
            {
                "id": "urn:li:geo:90010409",
                "name": "Greater Stockholm Metropolitan Area"
            },
            {
                "id": "urn:li:geo:109349863",
                "name": "111 44, Stockholm, Stockholm County, Sweden"
            },
            {
                "id": "urn:li:geo:101657786",
                "name": "Stockholm, New Jersey, United States"
            },
            {
                "id": "urn:li:geo:114797810",
                "name": "121 44, Stockholm, Stockholm County, Sweden"
            },
            {
                "id": "urn:li:geo:104312072",
                "name": "Täby, Stockholm County, Sweden"
            },
            {
                "id": "urn:li:geo:116778866",
                "name": "141 86, Stockholm, Stockholm County, Sweden"
            },
            {
                "id": "urn:li:geo:116784975",
                "name": "104 05, Stockholm, Stockholm County, Sweden"
            },
            {
                "id": "urn:li:geo:116819473",
                "name": "121 22, Stockholm, Stockholm County, Sweden"
            },
            {
                "id": "urn:li:geo:106042403",
                "name": "Huddinge kommun, Stockholm County, Sweden"
            },
            {
                "id": "urn:li:geo:103496818",
                "name": "Solna, Stockholm County, Sweden"
            },
            {
                "id": "urn:li:geo:105726220",
                "name": "Huddinge, Stockholm County, Sweden"
            },
            {
                "id": "urn:li:geo:116922081",
                "name": "121 27, Stockholm, Stockholm County, Sweden"
            },
            {
                "id": "urn:li:geo:116675062",
                "name": "100 12, Stockholm, Stockholm County, Sweden"
            },
            {
                "id": "urn:li:geo:116947599",
                "name": "121 26, Stockholm, Stockholm County, Sweden"
            },
            {
                "id": "urn:li:geo:112513080",
                "name": "118 65, Stockholm, Stockholm County, Sweden"
            },
            {
                "id": "urn:li:geo:109752268",
                "name": "118 26, Stockholm, Stockholm County, Sweden"
            },
            {
                "id": "urn:li:geo:116867498",
                "name": "100 29, Stockholm, Stockholm County, Sweden"
            },
            {
                "id": "urn:li:geo:113860433",
                "name": "128 62, Stockholm, Stockholm County, Sweden"
            }


* */
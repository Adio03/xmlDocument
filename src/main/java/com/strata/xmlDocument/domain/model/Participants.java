package com.strata.xmlDocument.domain.model;

import jakarta.xml.bind.annotation.*;
import lombok.*;

import java.util.List;

@XmlRootElement(name = "participants")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@XmlAccessorType(XmlAccessType.FIELD)
public class Participants {

    @XmlElement(name = "participant", required = true)
    private List<Participant> participants;


    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Participant {

        @XmlAttribute(name = "institutionCode", required = true)
        private String institutionCode;

        @XmlElement(name = "bicfic")
        private String bicfic;

        @XmlElement(name = "name", required = true)
        private String name;

        @XmlElement(name = "countryCode", required = true)
        private String countryCode;

        @XmlElement(name = "status", required = true)
        private String status;

        @XmlElement(name = "categoryCode", required = true)
        private String categoryCode;

        @XmlElement(name = "currencies")
        private Currencies currencies;

        @XmlElement(name = "operationsAllowed")
        private OperationsAllowed operationsAllowed;

    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Currencies {

        @XmlElement(name = "currency")
        private List<String> currency;

    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OperationsAllowed {

        @XmlElement(name = "operation")
        private List<String> operation;

    }
}
package models;

import lombok.Data;

@Data
public class CreateTestCaseResponse {
    Integer id;
    String name;
    Boolean automated;
    Boolean external;
    Long createdDate;
    String statusName;
    String statusColor;

}

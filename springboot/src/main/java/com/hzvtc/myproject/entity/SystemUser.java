package com.hzvtc.myproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author 熊新欣
 * @date 2020-12-08
 */
@Data
@Accessors(chain = true)
public class SystemUser implements Serializable {

    private static final long serialVersionUID = 1545931180051212983L;

    private Long id;

    @NotNull
    @Size(min = 2, max = 20)
    private String realName;

    @NotNull
    @Size(min = 5, max = 20)
    private String loginName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    transient private String password;

    private String icon;

    @Pattern(regexp = "([1][3,4,5,7,8,9][0-9]{9})|^$")
    private String cellphone;

    @Pattern(regexp = "(\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*)|^$")
    private String email;

    private List<SystemRole> userRole;

    private List<Long> userRoleId;

    private Set<String> permissions;

    private Long leaderId;

    private SystemUser leader;

    @NotNull
    private Long buildingId;

    private Building building;

    private Boolean isOnLine;

    private List<SystemFunction> functions;

    private LocalDateTime createTime;
}

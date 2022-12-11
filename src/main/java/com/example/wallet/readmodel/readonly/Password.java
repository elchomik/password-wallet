package com.example.wallet.readmodel.readonly;

import javax.persistence.*;

@Entity
@Table(name = "passwordUser")
public class Password {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer passwordId;
    private String passwd;
    private String webAddress;
    private String description;
    private String login;

    @Column(name = "userId")
    private Integer id;

    public Password(){}

    public Password(final String passwd, final String webAddress,final String description,
                    final String login, final Integer id) {
        this.passwd = passwd;
        this.webAddress = webAddress;
        this.description = description;
        this.login = login;
        this.id = id;
    }

    public Integer getPasswordId() {
        return passwordId;
    }

    public String getPasswd() {
        return passwd;
    }

    public String getWebAddress() {
        return webAddress;
    }

    public String getDescription() {
        return description;
    }

    public String getLogin() {
        return login;
    }

    public Integer getId() {
        return id;
    }
}

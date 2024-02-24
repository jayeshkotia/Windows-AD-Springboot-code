package com.soap.application.ldap.service;


import javax.naming.NamingException;
import javax.naming.directory.*;

import com.soap.application.ldap.Model.SearchUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import org.springframework.security.core.AuthenticationException;

import com.soap.application.ldap.Model.User;

import java.util.List;

@Service
public class LdapService {


    private final LdapTemplate ldapTemplate;
    @Autowired
    public LdapService(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }
    public void addUser(String username, String password) {
        ldapTemplate.bind(buildDn(username), null, buildAttributes(username, password));
}

    private String buildDn(String username) {
        return "CN=" + username + ",CN=Users,DC=dc,DC=trimblepoc,DC=local";
    }
    private Attributes buildAttributes(String username, String password) {
        Attributes attributes = new BasicAttributes();

        attributes.put("objectclass", "top");
        attributes.put("objectclass", "person");
        attributes.put("objectclass", "organizationalPerson");
        attributes.put("objectclass", "inetOrgPerson");

        attributes.put("uid", username);
        attributes.put("cn", username);
        attributes.put("sn", "Lastname");
        attributes.put("userPassword", password);

        return attributes;
    }

    public List<User> getUsers() {
        String filter = "(objectClass=" + "user" + ")";
        return ldapTemplate.search("CN=Users,DC=dc,DC=trimblepoc,DC=local", filter, (AttributesMapper<User>) attributes -> {
            User user = new User();
            String a = attributes.get("CN").toString();
            user.setUsername(attributes.get("CN").get().toString());
            return user;

        });
    }

    public List<SearchUser> search(String username,String password) {


        return ldapTemplate.search("CN=Users,DC=dc,DC=trimblepoc,DC=local","CN="+username, (AttributesMapper<SearchUser>) attributes -> {
            SearchUser user = new SearchUser();
            user.setUsername(attributes.get("CN").get().toString());
            user.setPassword(attributes.get("userPassword").get().toString());
            user.setCn(attributes.get("name").get().toString());
            user.setSn(attributes.get("sn").get().toString());
            return user;

        });
    }
        public void updateUser(String username, String sn) throws NamingException {

            String userDn = buildDn(username);
            ModificationItem[] modificationItems = new ModificationItem[1];
            modificationItems[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                    new BasicAttribute("sn", sn));

            ldapTemplate.modifyAttributes(userDn, modificationItems);
        }

    public void updateUserPassword(String username, String oldPassword, String newPassword) throws NamingException {
        try {
            ldapTemplate.authenticate(buildDn(username), "(objectClass=person)", oldPassword);
            String userDn = buildDn(username);
            ModificationItem[] modificationItems = new ModificationItem[1];
            modificationItems[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                    new BasicAttribute("userPassword", newPassword));

            ldapTemplate.modifyAttributes(userDn, modificationItems);
        } catch (AuthenticationException e) {
            throw new org.springframework.ldap.AuthenticationException();
        }
    }

    public void deleteUser(String username) throws NamingException {
        String userDn = buildDn(username);
        ldapTemplate.unbind(userDn);
    }
    private byte[] buildSecurityDescriptor() {
        return new byte[0];
    }
}


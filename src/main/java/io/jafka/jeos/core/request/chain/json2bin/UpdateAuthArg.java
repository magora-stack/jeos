package io.jafka.jeos.core.request.chain.json2bin;

import io.jafka.jeos.core.response.chain.account.RequiredAuth;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAuthArg {

    private String account;
    private RequiredAuth auth;
    private String parent;
    private String permission;
}

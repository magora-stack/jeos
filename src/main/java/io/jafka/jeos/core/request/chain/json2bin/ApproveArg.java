package io.jafka.jeos.core.request.chain.json2bin;

import io.jafka.jeos.core.response.chain.account.PermissionLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApproveArg {
	private String proposer;
	private String proposalName;
	private PermissionLevel permissionLevel;
}

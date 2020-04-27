package io.jafka.jeos.core.request.chain.json2bin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExecPropose {
	private String proposer;
	private String proposalName;
	private String executer;
}

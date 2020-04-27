package io.jafka.jeos.core.request.chain.json2bin;

import io.jafka.jeos.core.common.Authorization;
import io.jafka.jeos.core.common.transaction.PackedTransaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProposeArg {
	private String proposer;
	private String proposalName;
	private List<Authorization> requests;
	private PackedTransaction trx;
}

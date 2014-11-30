package org.maicoin;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import com.bccapi.bitlib.crypto.PublicKey;
import com.bccapi.bitlib.util.Base58;
import com.bccapi.bitlib.util.BitUtils;
import com.bccapi.bitlib.util.HashUtils;

public class BTCAddressValidator {
	public static Pattern REGEX_BITCOIN_ADDRESS = Pattern.compile("[13][a-km-zA-HJ-NP-Z0-9]{26,33}");

	public static boolean isValid(String s){
		return Base58.decodeChecked(s) != null;
	}
	public static void main(String args[]){
		String[] addrs = {
				"1Q1pE5vPGEEMqRcVRMbtBK842Y6Pzo6nK9",
				"1AGNa15ZQXAZUgFiqJ2i7Z2DPU2J6hW62i",
				"1Q1pE5vPGEEMqRcVRMbtBK842Y6Pzo6nJ9",
				"1AGNa15ZQXAZUgFiqJ2i7Z2DPU2J6hW62I",
				"1CKJehzL3GCWgAT5rfm4bNaznENoZx2qqH",
				"1J18yk7D353z3gRVcdbS7PV5Q8h5w6oWWG",
				"15iUDqk6nLmav3B1xUHPQivDpfMruVsu9f",
				"186pHM1up927B9MC27aaics6B8W7bfVpQn",
				"1KJTGpNzYsFibLmq9WaTGAXQbhRFUgnG3z",
				"1Fi57hAqyYYwaQVdA7a9qSKfiukBbt31G3",
				"3J98t1WpEZ73CNmQviecrnyiWrnqRhWNLy"
		};
		for (int i = 0; i < addrs.length; i++)
			System.out.println(addrs[i] + ' ' + addrs[i].length() + ' ' +  BTCAddressValidator.isValid(addrs[i]));
	}
}

package vc4.api.server;

import java.util.Arrays;

public class SUID {

	private byte[] suid;

	public SUID(byte[] suid) {
		super();
		this.suid = suid;
	}
	
	public byte[] getSuid() {
		return suid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(suid);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SUID other = (SUID) obj;
		if (!Arrays.equals(suid, other.suid))
			return false;
		return true;
	}
	
	
}

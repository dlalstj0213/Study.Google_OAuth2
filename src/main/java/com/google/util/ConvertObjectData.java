package com.google.util;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConvertObjectData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String clientId;
	private String clientPw;
}

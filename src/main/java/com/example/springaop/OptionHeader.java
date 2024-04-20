package com.example.springaop;

import java.math.BigDecimal;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class OptionHeader {

	private String derivativeType = null;

	private String instrumentName = null;
}

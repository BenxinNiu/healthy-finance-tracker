package com.brewery.app.finance.tracker.model;

import java.util.Arrays;

public class Enums {
 public enum ClientAccountType {
     VISA, MASTERCARD, AMEX, CHEQUING, SAVING, TFSA, INVESTMENT, CASH
 }

 public enum BalanceType {
     DEBT, ASSET
 }

 public enum TransactionType {
     EXPENSE, DEPOSIT
 }

 public enum CreditAccountType {
     VISA, MASTERCARD, AMEX, CHEQUING, SAVING, TFSA, INVESTMENT, CASH
 }

 public static <T extends Enum<T>> T toEnum(Class<T> enumName, String value) {
     return Arrays.stream(enumName.getEnumConstants())
             .filter(val -> val.toString().equals(value))
             .findFirst()
             .orElse(null);
 }

 public static <T extends Enum<T>> String toString(Enum<T> enumValue) {
     return enumValue != null
             ? enumValue.toString()
             : "";
 }
}

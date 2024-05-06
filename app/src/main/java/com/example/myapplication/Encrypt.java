package com.example.myapplication;

import com.eclipsesource.v8.V8;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Encrypt {

    public static void main(String[] args) throws IOException {
    }
    public Map<String,String> encrypt(String userName, String password, String key) throws IOException {
        String rsa = "";
        Map<String,String> map = new HashMap<>();
        V8 v8 = V8.createV8Runtime();
        v8.executeScript("function strEnc(data, firstKey, secondKey, thirdKey) {\n" +
                "\n" +
                "  var leng = data.length;\n" +
                "  var encData = \"\";\n" +
                "  var firstKeyBt, secondKeyBt, thirdKeyBt, firstLength, secondLength, thirdLength;\n" +
                "  if (firstKey != null && firstKey != \"\") {\n" +
                "    firstKeyBt = getKeyBytes(firstKey);\n" +
                "    firstLength = firstKeyBt.length;\n" +
                "  }\n" +
                "  if (secondKey != null && secondKey != \"\") {\n" +
                "    secondKeyBt = getKeyBytes(secondKey);\n" +
                "    secondLength = secondKeyBt.length;\n" +
                "  }\n" +
                "  if (thirdKey != null && thirdKey != \"\") {\n" +
                "    thirdKeyBt = getKeyBytes(thirdKey);\n" +
                "    thirdLength = thirdKeyBt.length;\n" +
                "  }\n" +
                "\n" +
                "  if (leng > 0) {\n" +
                "    if (leng < 4) {\n" +
                "      var bt = strToBt(data);\n" +
                "      var encByte;\n" +
                "      if (firstKey != null && firstKey != \"\" && secondKey != null && secondKey != \"\" && thirdKey != null && thirdKey != \"\") {\n" +
                "        var tempBt;\n" +
                "        var x, y, z;\n" +
                "        tempBt = bt;\n" +
                "        for (x = 0; x < firstLength; x++) {\n" +
                "          tempBt = enc(tempBt, firstKeyBt[x]);\n" +
                "        }\n" +
                "        for (y = 0; y < secondLength; y++) {\n" +
                "          tempBt = enc(tempBt, secondKeyBt[y]);\n" +
                "        }\n" +
                "        for (z = 0; z < thirdLength; z++) {\n" +
                "          tempBt = enc(tempBt, thirdKeyBt[z]);\n" +
                "        }\n" +
                "        encByte = tempBt;\n" +
                "      } else {\n" +
                "        if (firstKey != null && firstKey != \"\" && secondKey != null && secondKey != \"\") {\n" +
                "          var tempBt;\n" +
                "          var x, y;\n" +
                "          tempBt = bt;\n" +
                "          for (x = 0; x < firstLength; x++) {\n" +
                "            tempBt = enc(tempBt, firstKeyBt[x]);\n" +
                "          }\n" +
                "          for (y = 0; y < secondLength; y++) {\n" +
                "            tempBt = enc(tempBt, secondKeyBt[y]);\n" +
                "          }\n" +
                "          encByte = tempBt;\n" +
                "        } else {\n" +
                "          if (firstKey != null && firstKey != \"\") {\n" +
                "            var tempBt;\n" +
                "            var x = 0;\n" +
                "            tempBt = bt;\n" +
                "            for (x = 0; x < firstLength; x++) {\n" +
                "              tempBt = enc(tempBt, firstKeyBt[x]);\n" +
                "            }\n" +
                "            encByte = tempBt;\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "      encData = bt64ToHex(encByte);\n" +
                "    } else {\n" +
                "      var iterator = parseInt(leng / 4);\n" +
                "      var remainder = leng % 4;\n" +
                "      var i = 0;\n" +
                "      for (i = 0; i < iterator; i++) {\n" +
                "        var tempData = data.substring(i * 4 + 0, i * 4 + 4);\n" +
                "        var tempByte = strToBt(tempData);\n" +
                "        var encByte;\n" +
                "        if (firstKey != null && firstKey != \"\" && secondKey != null && secondKey != \"\" && thirdKey != null && thirdKey != \"\") {\n" +
                "          var tempBt;\n" +
                "          var x, y, z;\n" +
                "          tempBt = tempByte;\n" +
                "          for (x = 0; x < firstLength; x++) {\n" +
                "            tempBt = enc(tempBt, firstKeyBt[x]);\n" +
                "          }\n" +
                "          for (y = 0; y < secondLength; y++) {\n" +
                "            tempBt = enc(tempBt, secondKeyBt[y]);\n" +
                "          }\n" +
                "          for (z = 0; z < thirdLength; z++) {\n" +
                "            tempBt = enc(tempBt, thirdKeyBt[z]);\n" +
                "          }\n" +
                "          encByte = tempBt;\n" +
                "        } else {\n" +
                "          if (firstKey != null && firstKey != \"\" && secondKey != null && secondKey != \"\") {\n" +
                "            var tempBt;\n" +
                "            var x, y;\n" +
                "            tempBt = tempByte;\n" +
                "            for (x = 0; x < firstLength; x++) {\n" +
                "              tempBt = enc(tempBt, firstKeyBt[x]);\n" +
                "            }\n" +
                "            for (y = 0; y < secondLength; y++) {\n" +
                "              tempBt = enc(tempBt, secondKeyBt[y]);\n" +
                "            }\n" +
                "            encByte = tempBt;\n" +
                "          } else {\n" +
                "            if (firstKey != null && firstKey != \"\") {\n" +
                "              var tempBt;\n" +
                "              var x;\n" +
                "              tempBt = tempByte;\n" +
                "              for (x = 0; x < firstLength; x++) {\n" +
                "                tempBt = enc(tempBt, firstKeyBt[x]);\n" +
                "              }\n" +
                "              encByte = tempBt;\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "        encData += bt64ToHex(encByte);\n" +
                "      }\n" +
                "      if (remainder > 0) {\n" +
                "        var remainderData = data.substring(iterator * 4 + 0, leng);\n" +
                "        var tempByte = strToBt(remainderData);\n" +
                "        var encByte;\n" +
                "        if (firstKey != null && firstKey != \"\" && secondKey != null && secondKey != \"\" && thirdKey != null && thirdKey != \"\") {\n" +
                "          var tempBt;\n" +
                "          var x, y, z;\n" +
                "          tempBt = tempByte;\n" +
                "          for (x = 0; x < firstLength; x++) {\n" +
                "            tempBt = enc(tempBt, firstKeyBt[x]);\n" +
                "          }\n" +
                "          for (y = 0; y < secondLength; y++) {\n" +
                "            tempBt = enc(tempBt, secondKeyBt[y]);\n" +
                "          }\n" +
                "          for (z = 0; z < thirdLength; z++) {\n" +
                "            tempBt = enc(tempBt, thirdKeyBt[z]);\n" +
                "          }\n" +
                "          encByte = tempBt;\n" +
                "        } else {\n" +
                "          if (firstKey != null && firstKey != \"\" && secondKey != null && secondKey != \"\") {\n" +
                "            var tempBt;\n" +
                "            var x, y;\n" +
                "            tempBt = tempByte;\n" +
                "            for (x = 0; x < firstLength; x++) {\n" +
                "              tempBt = enc(tempBt, firstKeyBt[x]);\n" +
                "            }\n" +
                "            for (y = 0; y < secondLength; y++) {\n" +
                "              tempBt = enc(tempBt, secondKeyBt[y]);\n" +
                "            }\n" +
                "            encByte = tempBt;\n" +
                "          } else {\n" +
                "            if (firstKey != null && firstKey != \"\") {\n" +
                "              var tempBt;\n" +
                "              var x;\n" +
                "              tempBt = tempByte;\n" +
                "              for (x = 0; x < firstLength; x++) {\n" +
                "                tempBt = enc(tempBt, firstKeyBt[x]);\n" +
                "              }\n" +
                "              encByte = tempBt;\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "        encData += bt64ToHex(encByte);\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "  return encData;\n" +
                "}\n" +
                "\n" +
                "/*\n" +
                "* decrypt the encrypted string to the original string\n" +
                "*\n" +
                "* return  the original string\n" +
                "*/\n" +
                "function strDec(data, firstKey, secondKey, thirdKey) {\n" +
                "  var leng = data.length;\n" +
                "  var decStr = \"\";\n" +
                "  var firstKeyBt, secondKeyBt, thirdKeyBt, firstLength, secondLength, thirdLength;\n" +
                "  if (firstKey != null && firstKey != \"\") {\n" +
                "    firstKeyBt = getKeyBytes(firstKey);\n" +
                "    firstLength = firstKeyBt.length;\n" +
                "  }\n" +
                "  if (secondKey != null && secondKey != \"\") {\n" +
                "    secondKeyBt = getKeyBytes(secondKey);\n" +
                "    secondLength = secondKeyBt.length;\n" +
                "  }\n" +
                "  if (thirdKey != null && thirdKey != \"\") {\n" +
                "    thirdKeyBt = getKeyBytes(thirdKey);\n" +
                "    thirdLength = thirdKeyBt.length;\n" +
                "  }\n" +
                "\n" +
                "  var iterator = parseInt(leng / 16);\n" +
                "  var i = 0;\n" +
                "  for (i = 0; i < iterator; i++) {\n" +
                "    var tempData = data.substring(i * 16 + 0, i * 16 + 16);\n" +
                "    var strByte = hexToBt64(tempData);\n" +
                "    var intByte = new Array(64);\n" +
                "    var j = 0;\n" +
                "    for (j = 0; j < 64; j++) {\n" +
                "      intByte[j] = parseInt(strByte.substring(j, j + 1));\n" +
                "    }\n" +
                "    var decByte;\n" +
                "    if (firstKey != null && firstKey != \"\" && secondKey != null && secondKey != \"\" && thirdKey != null && thirdKey != \"\") {\n" +
                "      var tempBt;\n" +
                "      var x, y, z;\n" +
                "      tempBt = intByte;\n" +
                "      for (x = thirdLength - 1; x >= 0; x--) {\n" +
                "        tempBt = dec(tempBt, thirdKeyBt[x]);\n" +
                "      }\n" +
                "      for (y = secondLength - 1; y >= 0; y--) {\n" +
                "        tempBt = dec(tempBt, secondKeyBt[y]);\n" +
                "      }\n" +
                "      for (z = firstLength - 1; z >= 0; z--) {\n" +
                "        tempBt = dec(tempBt, firstKeyBt[z]);\n" +
                "      }\n" +
                "      decByte = tempBt;\n" +
                "    } else {\n" +
                "      if (firstKey != null && firstKey != \"\" && secondKey != null && secondKey != \"\") {\n" +
                "        var tempBt;\n" +
                "        var x, y, z;\n" +
                "        tempBt = intByte;\n" +
                "        for (x = secondLength - 1; x >= 0; x--) {\n" +
                "          tempBt = dec(tempBt, secondKeyBt[x]);\n" +
                "        }\n" +
                "        for (y = firstLength - 1; y >= 0; y--) {\n" +
                "          tempBt = dec(tempBt, firstKeyBt[y]);\n" +
                "        }\n" +
                "        decByte = tempBt;\n" +
                "      } else {\n" +
                "        if (firstKey != null && firstKey != \"\") {\n" +
                "          var tempBt;\n" +
                "          var x, y, z;\n" +
                "          tempBt = intByte;\n" +
                "          for (x = firstLength - 1; x >= 0; x--) {\n" +
                "            tempBt = dec(tempBt, firstKeyBt[x]);\n" +
                "          }\n" +
                "          decByte = tempBt;\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "    decStr += byteToString(decByte);\n" +
                "  }\n" +
                "  return decStr;\n" +
                "}\n" +
                "\n" +
                "/*\n" +
                "* chang the string into the bit array\n" +
                "*\n" +
                "* return bit array(it's length % 64 = 0)\n" +
                "*/\n" +
                "function getKeyBytes(key) {\n" +
                "  var keyBytes = new Array();\n" +
                "  var leng = key.length;\n" +
                "  var iterator = parseInt(leng / 4);\n" +
                "  var remainder = leng % 4;\n" +
                "  var i = 0;\n" +
                "  for (i = 0; i < iterator; i++) {\n" +
                "    keyBytes[i] = strToBt(key.substring(i * 4 + 0, i * 4 + 4));\n" +
                "  }\n" +
                "  if (remainder > 0) {\n" +
                "    keyBytes[i] = strToBt(key.substring(i * 4 + 0, leng));\n" +
                "  }\n" +
                "  return keyBytes;\n" +
                "}\n" +
                "\n" +
                "/*\n" +
                "* chang the string(it's length <= 4) into the bit array\n" +
                "*\n" +
                "* return bit array(it's length = 64)\n" +
                "*/\n" +
                "function strToBt(str) {\n" +
                "  var leng = str.length;\n" +
                "  var bt = new Array(64);\n" +
                "  if (leng < 4) {\n" +
                "    var i = 0, j = 0, p = 0, q = 0;\n" +
                "    for (i = 0; i < leng; i++) {\n" +
                "      var k = str.charCodeAt(i);\n" +
                "      for (j = 0; j < 16; j++) {\n" +
                "        var pow = 1, m = 0;\n" +
                "        for (m = 15; m > j; m--) {\n" +
                "          pow *= 2;\n" +
                "        }\n" +
                "        bt[16 * i + j] = parseInt(k / pow) % 2;\n" +
                "      }\n" +
                "    }\n" +
                "    for (p = leng; p < 4; p++) {\n" +
                "      var k = 0;\n" +
                "      for (q = 0; q < 16; q++) {\n" +
                "        var pow = 1, m = 0;\n" +
                "        for (m = 15; m > q; m--) {\n" +
                "          pow *= 2;\n" +
                "        }\n" +
                "        bt[16 * p + q] = parseInt(k / pow) % 2;\n" +
                "      }\n" +
                "    }\n" +
                "  } else {\n" +
                "    for (i = 0; i < 4; i++) {\n" +
                "      var k = str.charCodeAt(i);\n" +
                "      for (j = 0; j < 16; j++) {\n" +
                "        var pow = 1;\n" +
                "        for (m = 15; m > j; m--) {\n" +
                "          pow *= 2;\n" +
                "        }\n" +
                "        bt[16 * i + j] = parseInt(k / pow) % 2;\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "  return bt;\n" +
                "}\n" +
                "\n" +
                "/*\n" +
                "* chang the bit(it's length = 4) into the hex\n" +
                "*\n" +
                "* return hex\n" +
                "*/\n" +
                "function bt4ToHex(binary) {\n" +
                "  var hex;\n" +
                "  switch (binary) {\n" +
                "    case \"0000\" :\n" +
                "      hex = \"0\";\n" +
                "      break;\n" +
                "    case \"0001\" :\n" +
                "      hex = \"1\";\n" +
                "      break;\n" +
                "    case \"0010\" :\n" +
                "      hex = \"2\";\n" +
                "      break;\n" +
                "    case \"0011\" :\n" +
                "      hex = \"3\";\n" +
                "      break;\n" +
                "    case \"0100\" :\n" +
                "      hex = \"4\";\n" +
                "      break;\n" +
                "    case \"0101\" :\n" +
                "      hex = \"5\";\n" +
                "      break;\n" +
                "    case \"0110\" :\n" +
                "      hex = \"6\";\n" +
                "      break;\n" +
                "    case \"0111\" :\n" +
                "      hex = \"7\";\n" +
                "      break;\n" +
                "    case \"1000\" :\n" +
                "      hex = \"8\";\n" +
                "      break;\n" +
                "    case \"1001\" :\n" +
                "      hex = \"9\";\n" +
                "      break;\n" +
                "    case \"1010\" :\n" +
                "      hex = \"A\";\n" +
                "      break;\n" +
                "    case \"1011\" :\n" +
                "      hex = \"B\";\n" +
                "      break;\n" +
                "    case \"1100\" :\n" +
                "      hex = \"C\";\n" +
                "      break;\n" +
                "    case \"1101\" :\n" +
                "      hex = \"D\";\n" +
                "      break;\n" +
                "    case \"1110\" :\n" +
                "      hex = \"E\";\n" +
                "      break;\n" +
                "    case \"1111\" :\n" +
                "      hex = \"F\";\n" +
                "      break;\n" +
                "  }\n" +
                "  return hex;\n" +
                "}\n" +
                "\n" +
                "/*\n" +
                "* chang the hex into the bit(it's length = 4)\n" +
                "*\n" +
                "* return the bit(it's length = 4)\n" +
                "*/\n" +
                "function hexToBt4(hex) {\n" +
                "  var binary;\n" +
                "  switch (hex) {\n" +
                "    case \"0\" :\n" +
                "      binary = \"0000\";\n" +
                "      break;\n" +
                "    case \"1\" :\n" +
                "      binary = \"0001\";\n" +
                "      break;\n" +
                "    case \"2\" :\n" +
                "      binary = \"0010\";\n" +
                "      break;\n" +
                "    case \"3\" :\n" +
                "      binary = \"0011\";\n" +
                "      break;\n" +
                "    case \"4\" :\n" +
                "      binary = \"0100\";\n" +
                "      break;\n" +
                "    case \"5\" :\n" +
                "      binary = \"0101\";\n" +
                "      break;\n" +
                "    case \"6\" :\n" +
                "      binary = \"0110\";\n" +
                "      break;\n" +
                "    case \"7\" :\n" +
                "      binary = \"0111\";\n" +
                "      break;\n" +
                "    case \"8\" :\n" +
                "      binary = \"1000\";\n" +
                "      break;\n" +
                "    case \"9\" :\n" +
                "      binary = \"1001\";\n" +
                "      break;\n" +
                "    case \"A\" :\n" +
                "      binary = \"1010\";\n" +
                "      break;\n" +
                "    case \"B\" :\n" +
                "      binary = \"1011\";\n" +
                "      break;\n" +
                "    case \"C\" :\n" +
                "      binary = \"1100\";\n" +
                "      break;\n" +
                "    case \"D\" :\n" +
                "      binary = \"1101\";\n" +
                "      break;\n" +
                "    case \"E\" :\n" +
                "      binary = \"1110\";\n" +
                "      break;\n" +
                "    case \"F\" :\n" +
                "      binary = \"1111\";\n" +
                "      break;\n" +
                "  }\n" +
                "  return binary;\n" +
                "}\n" +
                "\n" +
                "/*\n" +
                "* chang the bit(it's length = 64) into the string\n" +
                "*\n" +
                "* return string\n" +
                "*/\n" +
                "function byteToString(byteData) {\n" +
                "  var str = \"\";\n" +
                "  for (i = 0; i < 4; i++) {\n" +
                "    var count = 0;\n" +
                "    for (j = 0; j < 16; j++) {\n" +
                "      var pow = 1;\n" +
                "      for (m = 15; m > j; m--) {\n" +
                "        pow *= 2;\n" +
                "      }\n" +
                "      count += byteData[16 * i + j] * pow;\n" +
                "    }\n" +
                "    if (count != 0) {\n" +
                "      str += String.fromCharCode(count);\n" +
                "    }\n" +
                "  }\n" +
                "  return str;\n" +
                "}\n" +
                "\n" +
                "function bt64ToHex(byteData) {\n" +
                "  var hex = \"\";\n" +
                "  for (i = 0; i < 16; i++) {\n" +
                "    var bt = \"\";\n" +
                "    for (j = 0; j < 4; j++) {\n" +
                "      bt += byteData[i * 4 + j];\n" +
                "    }\n" +
                "    hex += bt4ToHex(bt);\n" +
                "  }\n" +
                "  return hex;\n" +
                "}\n" +
                "\n" +
                "function hexToBt64(hex) {\n" +
                "  var binary = \"\";\n" +
                "  for (i = 0; i < 16; i++) {\n" +
                "    binary += hexToBt4(hex.substring(i, i + 1));\n" +
                "  }\n" +
                "  return binary;\n" +
                "}\n" +
                "\n" +
                "/*\n" +
                "* the 64 bit des core arithmetic\n" +
                "*/\n" +
                "\n" +
                "function enc(dataByte, keyByte) {\n" +
                "  var keys = generateKeys(keyByte);\n" +
                "  var ipByte = initPermute(dataByte);\n" +
                "  var ipLeft = new Array(32);\n" +
                "  var ipRight = new Array(32);\n" +
                "  var tempLeft = new Array(32);\n" +
                "  var i = 0, j = 0, k = 0, m = 0, n = 0;\n" +
                "  for (k = 0; k < 32; k++) {\n" +
                "    ipLeft[k] = ipByte[k];\n" +
                "    ipRight[k] = ipByte[32 + k];\n" +
                "  }\n" +
                "  for (i = 0; i < 16; i++) {\n" +
                "    for (j = 0; j < 32; j++) {\n" +
                "      tempLeft[j] = ipLeft[j];\n" +
                "      ipLeft[j] = ipRight[j];\n" +
                "    }\n" +
                "    var key = new Array(48);\n" +
                "    for (m = 0; m < 48; m++) {\n" +
                "      key[m] = keys[i][m];\n" +
                "    }\n" +
                "    var tempRight = xor(pPermute(sBoxPermute(xor(expandPermute(ipRight), key))), tempLeft);\n" +
                "    for (n = 0; n < 32; n++) {\n" +
                "      ipRight[n] = tempRight[n];\n" +
                "    }\n" +
                "\n" +
                "  }\n" +
                "\n" +
                "\n" +
                "  var finalData = new Array(64);\n" +
                "  for (i = 0; i < 32; i++) {\n" +
                "    finalData[i] = ipRight[i];\n" +
                "    finalData[32 + i] = ipLeft[i];\n" +
                "  }\n" +
                "  return finallyPermute(finalData);\n" +
                "}\n" +
                "\n" +
                "function dec(dataByte, keyByte) {\n" +
                "  var keys = generateKeys(keyByte);\n" +
                "  var ipByte = initPermute(dataByte);\n" +
                "  var ipLeft = new Array(32);\n" +
                "  var ipRight = new Array(32);\n" +
                "  var tempLeft = new Array(32);\n" +
                "  var i = 0, j = 0, k = 0, m = 0, n = 0;\n" +
                "  for (k = 0; k < 32; k++) {\n" +
                "    ipLeft[k] = ipByte[k];\n" +
                "    ipRight[k] = ipByte[32 + k];\n" +
                "  }\n" +
                "  for (i = 15; i >= 0; i--) {\n" +
                "    for (j = 0; j < 32; j++) {\n" +
                "      tempLeft[j] = ipLeft[j];\n" +
                "      ipLeft[j] = ipRight[j];\n" +
                "    }\n" +
                "    var key = new Array(48);\n" +
                "    for (m = 0; m < 48; m++) {\n" +
                "      key[m] = keys[i][m];\n" +
                "    }\n" +
                "\n" +
                "    var tempRight = xor(pPermute(sBoxPermute(xor(expandPermute(ipRight), key))), tempLeft);\n" +
                "    for (n = 0; n < 32; n++) {\n" +
                "      ipRight[n] = tempRight[n];\n" +
                "    }\n" +
                "  }\n" +
                "\n" +
                "\n" +
                "  var finalData = new Array(64);\n" +
                "  for (i = 0; i < 32; i++) {\n" +
                "    finalData[i] = ipRight[i];\n" +
                "    finalData[32 + i] = ipLeft[i];\n" +
                "  }\n" +
                "  return finallyPermute(finalData);\n" +
                "}\n" +
                "\n" +
                "function initPermute(originalData) {\n" +
                "  var ipByte = new Array(64);\n" +
                "  for (i = 0, m = 1, n = 0; i < 4; i++, m += 2, n += 2) {\n" +
                "    for (j = 7, k = 0; j >= 0; j--, k++) {\n" +
                "      ipByte[i * 8 + k] = originalData[j * 8 + m];\n" +
                "      ipByte[i * 8 + k + 32] = originalData[j * 8 + n];\n" +
                "    }\n" +
                "  }\n" +
                "  return ipByte;\n" +
                "}\n" +
                "\n" +
                "function expandPermute(rightData) {\n" +
                "  var epByte = new Array(48);\n" +
                "  for (i = 0; i < 8; i++) {\n" +
                "    if (i == 0) {\n" +
                "      epByte[i * 6 + 0] = rightData[31];\n" +
                "    } else {\n" +
                "      epByte[i * 6 + 0] = rightData[i * 4 - 1];\n" +
                "    }\n" +
                "    epByte[i * 6 + 1] = rightData[i * 4 + 0];\n" +
                "    epByte[i * 6 + 2] = rightData[i * 4 + 1];\n" +
                "    epByte[i * 6 + 3] = rightData[i * 4 + 2];\n" +
                "    epByte[i * 6 + 4] = rightData[i * 4 + 3];\n" +
                "    if (i == 7) {\n" +
                "      epByte[i * 6 + 5] = rightData[0];\n" +
                "    } else {\n" +
                "      epByte[i * 6 + 5] = rightData[i * 4 + 4];\n" +
                "    }\n" +
                "  }\n" +
                "  return epByte;\n" +
                "}\n" +
                "\n" +
                "function xor(byteOne, byteTwo) {\n" +
                "  var xorByte = new Array(byteOne.length);\n" +
                "  for (i = 0; i < byteOne.length; i++) {\n" +
                "    xorByte[i] = byteOne[i] ^ byteTwo[i];\n" +
                "  }\n" +
                "  return xorByte;\n" +
                "}\n" +
                "\n" +
                "function sBoxPermute(expandByte) {\n" +
                "\n" +
                "  var sBoxByte = new Array(32);\n" +
                "  var binary = \"\";\n" +
                "  var s1 = [\n" +
                "    [14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7],\n" +
                "    [0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8],\n" +
                "    [4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0],\n" +
                "    [15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13]];\n" +
                "\n" +
                "  /* Table - s2 */\n" +
                "  var s2 = [\n" +
                "    [15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10],\n" +
                "    [3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5],\n" +
                "    [0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15],\n" +
                "    [13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9]];\n" +
                "\n" +
                "  /* Table - s3 */\n" +
                "  var s3 = [\n" +
                "    [10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8],\n" +
                "    [13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1],\n" +
                "    [13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7],\n" +
                "    [1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12]];\n" +
                "  /* Table - s4 */\n" +
                "  var s4 = [\n" +
                "    [7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15],\n" +
                "    [13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9],\n" +
                "    [10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4],\n" +
                "    [3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14]];\n" +
                "\n" +
                "  /* Table - s5 */\n" +
                "  var s5 = [\n" +
                "    [2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9],\n" +
                "    [14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6],\n" +
                "    [4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14],\n" +
                "    [11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3]];\n" +
                "\n" +
                "  /* Table - s6 */\n" +
                "  var s6 = [\n" +
                "    [12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11],\n" +
                "    [10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8],\n" +
                "    [9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6],\n" +
                "    [4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13]];\n" +
                "\n" +
                "  /* Table - s7 */\n" +
                "  var s7 = [\n" +
                "    [4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1],\n" +
                "    [13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6],\n" +
                "    [1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2],\n" +
                "    [6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12]];\n" +
                "\n" +
                "  /* Table - s8 */\n" +
                "  var s8 = [\n" +
                "    [13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7],\n" +
                "    [1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2],\n" +
                "    [7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8],\n" +
                "    [2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11]];\n" +
                "\n" +
                "  for (m = 0; m < 8; m++) {\n" +
                "    var i = 0, j = 0;\n" +
                "    i = expandByte[m * 6 + 0] * 2 + expandByte[m * 6 + 5];\n" +
                "    j = expandByte[m * 6 + 1] * 2 * 2 * 2\n" +
                "      + expandByte[m * 6 + 2] * 2 * 2\n" +
                "      + expandByte[m * 6 + 3] * 2\n" +
                "      + expandByte[m * 6 + 4];\n" +
                "    switch (m) {\n" +
                "      case 0 :\n" +
                "        binary = getBoxBinary(s1[i][j]);\n" +
                "        break;\n" +
                "      case 1 :\n" +
                "        binary = getBoxBinary(s2[i][j]);\n" +
                "        break;\n" +
                "      case 2 :\n" +
                "        binary = getBoxBinary(s3[i][j]);\n" +
                "        break;\n" +
                "      case 3 :\n" +
                "        binary = getBoxBinary(s4[i][j]);\n" +
                "        break;\n" +
                "      case 4 :\n" +
                "        binary = getBoxBinary(s5[i][j]);\n" +
                "        break;\n" +
                "      case 5 :\n" +
                "        binary = getBoxBinary(s6[i][j]);\n" +
                "        break;\n" +
                "      case 6 :\n" +
                "        binary = getBoxBinary(s7[i][j]);\n" +
                "        break;\n" +
                "      case 7 :\n" +
                "        binary = getBoxBinary(s8[i][j]);\n" +
                "        break;\n" +
                "    }\n" +
                "    sBoxByte[m * 4 + 0] = parseInt(binary.substring(0, 1));\n" +
                "    sBoxByte[m * 4 + 1] = parseInt(binary.substring(1, 2));\n" +
                "    sBoxByte[m * 4 + 2] = parseInt(binary.substring(2, 3));\n" +
                "    sBoxByte[m * 4 + 3] = parseInt(binary.substring(3, 4));\n" +
                "  }\n" +
                "  return sBoxByte;\n" +
                "}\n" +
                "\n" +
                "function pPermute(sBoxByte) {\n" +
                "  var pBoxPermute = new Array(32);\n" +
                "  pBoxPermute[0] = sBoxByte[15];\n" +
                "  pBoxPermute[1] = sBoxByte[6];\n" +
                "  pBoxPermute[2] = sBoxByte[19];\n" +
                "  pBoxPermute[3] = sBoxByte[20];\n" +
                "  pBoxPermute[4] = sBoxByte[28];\n" +
                "  pBoxPermute[5] = sBoxByte[11];\n" +
                "  pBoxPermute[6] = sBoxByte[27];\n" +
                "  pBoxPermute[7] = sBoxByte[16];\n" +
                "  pBoxPermute[8] = sBoxByte[0];\n" +
                "  pBoxPermute[9] = sBoxByte[14];\n" +
                "  pBoxPermute[10] = sBoxByte[22];\n" +
                "  pBoxPermute[11] = sBoxByte[25];\n" +
                "  pBoxPermute[12] = sBoxByte[4];\n" +
                "  pBoxPermute[13] = sBoxByte[17];\n" +
                "  pBoxPermute[14] = sBoxByte[30];\n" +
                "  pBoxPermute[15] = sBoxByte[9];\n" +
                "  pBoxPermute[16] = sBoxByte[1];\n" +
                "  pBoxPermute[17] = sBoxByte[7];\n" +
                "  pBoxPermute[18] = sBoxByte[23];\n" +
                "  pBoxPermute[19] = sBoxByte[13];\n" +
                "  pBoxPermute[20] = sBoxByte[31];\n" +
                "  pBoxPermute[21] = sBoxByte[26];\n" +
                "  pBoxPermute[22] = sBoxByte[2];\n" +
                "  pBoxPermute[23] = sBoxByte[8];\n" +
                "  pBoxPermute[24] = sBoxByte[18];\n" +
                "  pBoxPermute[25] = sBoxByte[12];\n" +
                "  pBoxPermute[26] = sBoxByte[29];\n" +
                "  pBoxPermute[27] = sBoxByte[5];\n" +
                "  pBoxPermute[28] = sBoxByte[21];\n" +
                "  pBoxPermute[29] = sBoxByte[10];\n" +
                "  pBoxPermute[30] = sBoxByte[3];\n" +
                "  pBoxPermute[31] = sBoxByte[24];\n" +
                "  return pBoxPermute;\n" +
                "}\n" +
                "\n" +
                "function finallyPermute(endByte) {\n" +
                "  var fpByte = new Array(64);\n" +
                "  fpByte[0] = endByte[39];\n" +
                "  fpByte[1] = endByte[7];\n" +
                "  fpByte[2] = endByte[47];\n" +
                "  fpByte[3] = endByte[15];\n" +
                "  fpByte[4] = endByte[55];\n" +
                "  fpByte[5] = endByte[23];\n" +
                "  fpByte[6] = endByte[63];\n" +
                "  fpByte[7] = endByte[31];\n" +
                "  fpByte[8] = endByte[38];\n" +
                "  fpByte[9] = endByte[6];\n" +
                "  fpByte[10] = endByte[46];\n" +
                "  fpByte[11] = endByte[14];\n" +
                "  fpByte[12] = endByte[54];\n" +
                "  fpByte[13] = endByte[22];\n" +
                "  fpByte[14] = endByte[62];\n" +
                "  fpByte[15] = endByte[30];\n" +
                "  fpByte[16] = endByte[37];\n" +
                "  fpByte[17] = endByte[5];\n" +
                "  fpByte[18] = endByte[45];\n" +
                "  fpByte[19] = endByte[13];\n" +
                "  fpByte[20] = endByte[53];\n" +
                "  fpByte[21] = endByte[21];\n" +
                "  fpByte[22] = endByte[61];\n" +
                "  fpByte[23] = endByte[29];\n" +
                "  fpByte[24] = endByte[36];\n" +
                "  fpByte[25] = endByte[4];\n" +
                "  fpByte[26] = endByte[44];\n" +
                "  fpByte[27] = endByte[12];\n" +
                "  fpByte[28] = endByte[52];\n" +
                "  fpByte[29] = endByte[20];\n" +
                "  fpByte[30] = endByte[60];\n" +
                "  fpByte[31] = endByte[28];\n" +
                "  fpByte[32] = endByte[35];\n" +
                "  fpByte[33] = endByte[3];\n" +
                "  fpByte[34] = endByte[43];\n" +
                "  fpByte[35] = endByte[11];\n" +
                "  fpByte[36] = endByte[51];\n" +
                "  fpByte[37] = endByte[19];\n" +
                "  fpByte[38] = endByte[59];\n" +
                "  fpByte[39] = endByte[27];\n" +
                "  fpByte[40] = endByte[34];\n" +
                "  fpByte[41] = endByte[2];\n" +
                "  fpByte[42] = endByte[42];\n" +
                "  fpByte[43] = endByte[10];\n" +
                "  fpByte[44] = endByte[50];\n" +
                "  fpByte[45] = endByte[18];\n" +
                "  fpByte[46] = endByte[58];\n" +
                "  fpByte[47] = endByte[26];\n" +
                "  fpByte[48] = endByte[33];\n" +
                "  fpByte[49] = endByte[1];\n" +
                "  fpByte[50] = endByte[41];\n" +
                "  fpByte[51] = endByte[9];\n" +
                "  fpByte[52] = endByte[49];\n" +
                "  fpByte[53] = endByte[17];\n" +
                "  fpByte[54] = endByte[57];\n" +
                "  fpByte[55] = endByte[25];\n" +
                "  fpByte[56] = endByte[32];\n" +
                "  fpByte[57] = endByte[0];\n" +
                "  fpByte[58] = endByte[40];\n" +
                "  fpByte[59] = endByte[8];\n" +
                "  fpByte[60] = endByte[48];\n" +
                "  fpByte[61] = endByte[16];\n" +
                "  fpByte[62] = endByte[56];\n" +
                "  fpByte[63] = endByte[24];\n" +
                "  return fpByte;\n" +
                "}\n" +
                "\n" +
                "function getBoxBinary(i) {\n" +
                "  var binary = \"\";\n" +
                "  switch (i) {\n" +
                "    case 0 :\n" +
                "      binary = \"0000\";\n" +
                "      break;\n" +
                "    case 1 :\n" +
                "      binary = \"0001\";\n" +
                "      break;\n" +
                "    case 2 :\n" +
                "      binary = \"0010\";\n" +
                "      break;\n" +
                "    case 3 :\n" +
                "      binary = \"0011\";\n" +
                "      break;\n" +
                "    case 4 :\n" +
                "      binary = \"0100\";\n" +
                "      break;\n" +
                "    case 5 :\n" +
                "      binary = \"0101\";\n" +
                "      break;\n" +
                "    case 6 :\n" +
                "      binary = \"0110\";\n" +
                "      break;\n" +
                "    case 7 :\n" +
                "      binary = \"0111\";\n" +
                "      break;\n" +
                "    case 8 :\n" +
                "      binary = \"1000\";\n" +
                "      break;\n" +
                "    case 9 :\n" +
                "      binary = \"1001\";\n" +
                "      break;\n" +
                "    case 10 :\n" +
                "      binary = \"1010\";\n" +
                "      break;\n" +
                "    case 11 :\n" +
                "      binary = \"1011\";\n" +
                "      break;\n" +
                "    case 12 :\n" +
                "      binary = \"1100\";\n" +
                "      break;\n" +
                "    case 13 :\n" +
                "      binary = \"1101\";\n" +
                "      break;\n" +
                "    case 14 :\n" +
                "      binary = \"1110\";\n" +
                "      break;\n" +
                "    case 15 :\n" +
                "      binary = \"1111\";\n" +
                "      break;\n" +
                "  }\n" +
                "  return binary;\n" +
                "}\n" +
                "\n" +
                "/*\n" +
                "* generate 16 keys for xor\n" +
                "*\n" +
                "*/\n" +
                "function generateKeys(keyByte) {\n" +
                "  var key = new Array(56);\n" +
                "  var keys = new Array();\n" +
                "\n" +
                "  keys[0] = new Array();\n" +
                "  keys[1] = new Array();\n" +
                "  keys[2] = new Array();\n" +
                "  keys[3] = new Array();\n" +
                "  keys[4] = new Array();\n" +
                "  keys[5] = new Array();\n" +
                "  keys[6] = new Array();\n" +
                "  keys[7] = new Array();\n" +
                "  keys[8] = new Array();\n" +
                "  keys[9] = new Array();\n" +
                "  keys[10] = new Array();\n" +
                "  keys[11] = new Array();\n" +
                "  keys[12] = new Array();\n" +
                "  keys[13] = new Array();\n" +
                "  keys[14] = new Array();\n" +
                "  keys[15] = new Array();\n" +
                "  var loop = [1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1];\n" +
                "\n" +
                "  for (i = 0; i < 7; i++) {\n" +
                "    for (j = 0, k = 7; j < 8; j++, k--) {\n" +
                "      key[i * 8 + j] = keyByte[8 * k + i];\n" +
                "    }\n" +
                "  }\n" +
                "\n" +
                "  var i = 0;\n" +
                "  for (i = 0; i < 16; i++) {\n" +
                "    var tempLeft = 0;\n" +
                "    var tempRight = 0;\n" +
                "    for (j = 0; j < loop[i]; j++) {\n" +
                "      tempLeft = key[0];\n" +
                "      tempRight = key[28];\n" +
                "      for (k = 0; k < 27; k++) {\n" +
                "        key[k] = key[k + 1];\n" +
                "        key[28 + k] = key[29 + k];\n" +
                "      }\n" +
                "      key[27] = tempLeft;\n" +
                "      key[55] = tempRight;\n" +
                "    }\n" +
                "    var tempKey = new Array(48);\n" +
                "    tempKey[0] = key[13];\n" +
                "    tempKey[1] = key[16];\n" +
                "    tempKey[2] = key[10];\n" +
                "    tempKey[3] = key[23];\n" +
                "    tempKey[4] = key[0];\n" +
                "    tempKey[5] = key[4];\n" +
                "    tempKey[6] = key[2];\n" +
                "    tempKey[7] = key[27];\n" +
                "    tempKey[8] = key[14];\n" +
                "    tempKey[9] = key[5];\n" +
                "    tempKey[10] = key[20];\n" +
                "    tempKey[11] = key[9];\n" +
                "    tempKey[12] = key[22];\n" +
                "    tempKey[13] = key[18];\n" +
                "    tempKey[14] = key[11];\n" +
                "    tempKey[15] = key[3];\n" +
                "    tempKey[16] = key[25];\n" +
                "    tempKey[17] = key[7];\n" +
                "    tempKey[18] = key[15];\n" +
                "    tempKey[19] = key[6];\n" +
                "    tempKey[20] = key[26];\n" +
                "    tempKey[21] = key[19];\n" +
                "    tempKey[22] = key[12];\n" +
                "    tempKey[23] = key[1];\n" +
                "    tempKey[24] = key[40];\n" +
                "    tempKey[25] = key[51];\n" +
                "    tempKey[26] = key[30];\n" +
                "    tempKey[27] = key[36];\n" +
                "    tempKey[28] = key[46];\n" +
                "    tempKey[29] = key[54];\n" +
                "    tempKey[30] = key[29];\n" +
                "    tempKey[31] = key[39];\n" +
                "    tempKey[32] = key[50];\n" +
                "    tempKey[33] = key[44];\n" +
                "    tempKey[34] = key[32];\n" +
                "    tempKey[35] = key[47];\n" +
                "    tempKey[36] = key[43];\n" +
                "    tempKey[37] = key[48];\n" +
                "    tempKey[38] = key[38];\n" +
                "    tempKey[39] = key[55];\n" +
                "    tempKey[40] = key[33];\n" +
                "    tempKey[41] = key[52];\n" +
                "    tempKey[42] = key[45];\n" +
                "    tempKey[43] = key[41];\n" +
                "    tempKey[44] = key[49];\n" +
                "    tempKey[45] = key[35];\n" +
                "    tempKey[46] = key[28];\n" +
                "    tempKey[47] = key[31];\n" +
                "    switch (i) {\n" +
                "      case 0:\n" +
                "        for (m = 0; m < 48; m++) {\n" +
                "          keys[0][m] = tempKey[m];\n" +
                "        }\n" +
                "        break;\n" +
                "      case 1:\n" +
                "        for (m = 0; m < 48; m++) {\n" +
                "          keys[1][m] = tempKey[m];\n" +
                "        }\n" +
                "        break;\n" +
                "      case 2:\n" +
                "        for (m = 0; m < 48; m++) {\n" +
                "          keys[2][m] = tempKey[m];\n" +
                "        }\n" +
                "        break;\n" +
                "      case 3:\n" +
                "        for (m = 0; m < 48; m++) {\n" +
                "          keys[3][m] = tempKey[m];\n" +
                "        }\n" +
                "        break;\n" +
                "      case 4:\n" +
                "        for (m = 0; m < 48; m++) {\n" +
                "          keys[4][m] = tempKey[m];\n" +
                "        }\n" +
                "        break;\n" +
                "      case 5:\n" +
                "        for (m = 0; m < 48; m++) {\n" +
                "          keys[5][m] = tempKey[m];\n" +
                "        }\n" +
                "        break;\n" +
                "      case 6:\n" +
                "        for (m = 0; m < 48; m++) {\n" +
                "          keys[6][m] = tempKey[m];\n" +
                "        }\n" +
                "        break;\n" +
                "      case 7:\n" +
                "        for (m = 0; m < 48; m++) {\n" +
                "          keys[7][m] = tempKey[m];\n" +
                "        }\n" +
                "        break;\n" +
                "      case 8:\n" +
                "        for (m = 0; m < 48; m++) {\n" +
                "          keys[8][m] = tempKey[m];\n" +
                "        }\n" +
                "        break;\n" +
                "      case 9:\n" +
                "        for (m = 0; m < 48; m++) {\n" +
                "          keys[9][m] = tempKey[m];\n" +
                "        }\n" +
                "        break;\n" +
                "      case 10:\n" +
                "        for (m = 0; m < 48; m++) {\n" +
                "          keys[10][m] = tempKey[m];\n" +
                "        }\n" +
                "        break;\n" +
                "      case 11:\n" +
                "        for (m = 0; m < 48; m++) {\n" +
                "          keys[11][m] = tempKey[m];\n" +
                "        }\n" +
                "        break;\n" +
                "      case 12:\n" +
                "        for (m = 0; m < 48; m++) {\n" +
                "          keys[12][m] = tempKey[m];\n" +
                "        }\n" +
                "        break;\n" +
                "      case 13:\n" +
                "        for (m = 0; m < 48; m++) {\n" +
                "          keys[13][m] = tempKey[m];\n" +
                "        }\n" +
                "        break;\n" +
                "      case 14:\n" +
                "        for (m = 0; m < 48; m++) {\n" +
                "          keys[14][m] = tempKey[m];\n" +
                "        }\n" +
                "        break;\n" +
                "      case 15:\n" +
                "        for (m = 0; m < 48; m++) {\n" +
                "          keys[15][m] = tempKey[m];\n" +
                "        }\n" +
                "        break;\n" +
                "    }\n" +
                "  }\n" +
                "  return keys;\n" +
                "}\n" +
                "\n" +
                "function encrypt(u,p,lt) {\n" +
                "  if (u == \"\") {\n" +
                "    u.focus();\n" +
                "    u.parent().addClass(\"login_error_border\");\n" +
                "    return;\n" +
                "  }\n" +
                "\n" +
                "  if (p == \"\") {\n" +
                "    p.focus();\n" +
                "    p.parent().addClass(\"login_error_border\");\n" +
                "    return;\n" +
                "  }\n" +
                "\n" +
                "  var ul = u.length;\n" +
                "  var pl = p.length;\n" +
                "  var rsa = strEnc(u + p + lt, '1', '2', '3');\n" +
                "  return rsa;\n" +
                "}\n");
        rsa = (String) v8.executeJSFunction("encrypt", userName, password, key);
        map.put("ul", String.valueOf(userName.length()));
        map.put("pl", String.valueOf(password.length()));
        map.put("rsa", rsa);
        System.out.println("");

        return map;
    }
}

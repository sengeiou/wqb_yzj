package com.wqb.model.vomodel;

import java.io.Serializable;

public class ParentCodeMapping implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4838469002230247121L;
    //private int subCode;
    private int[] arr1;
    //private int[] arr2;

    public ParentCodeMapping() {
        this.arr1 = setDir1();
    }

    public int[] setDir1() {
        return new int[]
                {
                        1001, 1002, 1012, 1101, 1121, 1122, 1123, 1131, 1132, 1221, 1321, 1401, 1402, 1403,
                        1404, 1405, 1406, 1408, 1411, 1501, 1503, 1511, 1521, 1531, 1601, 1604, 1605, 1606, 1701,
                        1711, 1801, 1811, 1901, 2702, 3101, 3201, 3202, 4201, 5001, 5101, 5201, 5301, 6401, 6402,
                        6403, 6601, 6602, 6603, 6701, 6711, 6801
                };
    }

/*	public int[] setDir2(){
		return new int[]
			{
				1231,1407,1471,1502,1512,1532,1602,1603,1702,1703,2001,2101,2201,2202,
				2203,2211,2221,2231,2232,2241,2314,2401,2501,2502,2701,2711,2801,2901,4001,
				4002,4101,4103,4104,6001,6051,6101,6111,6301,6901
			};
	}*/

    public String getDir(String code) {
        String replace = code.trim().substring(0, 4);
        Integer subCode = Integer.valueOf(replace);
        int[] a1 = this.arr1;
        for (int i = 0; i < a1.length; i++) {
            if (subCode == a1[i]) {
                return "1";
            }
        }
        return "2";
    }


}

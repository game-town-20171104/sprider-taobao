package com.ylfin.spider.utils;

public class PasswordUtils {

    /**
     *
     * @param totalNum 总个数
     * @param letterNum 字母个数
     * @return
     */
    public static String generatePassword(int totalNum,int letterNum){
        String[] seed1={"A","B","C","D","E","F","G","H","J","K","M","N","Q","R","S","T","U","V","W","X","Y","Z"};
        String[] seed2 = {"1","2","3","4","5","6","7","8","9"};

        int seed1Num =letterNum; //必定会满足的数量，可自行修改
        int seed2Num =totalNum-seed1Num;
        StringBuilder sb = new StringBuilder();
//        boolean hasLatter =false;
        int seed1Count=0;
        for(int i=0;i<totalNum;i++){
            //保证seed1不超标
            if(seed1Count>=seed1Num){
                sb.append(seed2[SpiderUtils.randomInteger(0,seed2.length-1)]);
                continue;
            }
            //保证有足够的seed1
            if (seed1Count<seed1Num&&i>=totalNum-seed1Num){
                sb.append(seed1[SpiderUtils.randomInteger(0,seed1.length-1)]);
                seed1Count++;
                continue;
            }

            int randomNum =SpiderUtils.randomInteger(0,seed1.length+seed2.length-1);
            if(randomNum>=seed2.length){
                seed1Count++;
                sb.append(seed1[randomNum-seed2.length]);
            }else {
                sb.append(seed2[randomNum]);
            }

        }

        return sb.toString();
    }


    /**
     * 默认7个数字，1个字母
     * @return
     */
    public static String generatePassword(){
       return generatePassword(8,1);
    }

    public static void main(String[] args) {
        for (int i=0;i<10;i++){
            System.out.println(generatePassword());
        }
    }
}

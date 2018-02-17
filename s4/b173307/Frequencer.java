package s4.b173307; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
import java.lang.*;
import s4.specification.*;

//演習3&4
public class Frequencer implements FrequencerInterface{
    // Code to start with: This code is not working, but good start point to work.
    byte [] myTarget;
    byte [] mySpace;
    boolean targetReady = false;
    boolean spaceReady = false;
    int [] suffixArray;
    // The variable, "suffixArray" is the sorted array of all suffixes of mySpace.
    // Each suffix is expressed by a interger, which is the starting position in mySpace.
    // The following is the code to print the variable
    private void printSuffixArray() {
        if(spaceReady) {
            for(int i=0; i< mySpace.length; i++) {
                //    System.out.println(suffixArray[i]);
                int s = suffixArray[i];
                for(int j=s;j<mySpace.length;j++) {
                    System.out.write(mySpace[j]);
                }
                System.out.write('\n');
            }
        }
    }
    private int suffixCompare(int i, int j) {
        // comparing two suffixes by dictionary order.
        // i and j denoetes suffix_i, and suffix_j
        // if suffix_i > suffix_j, it returns 1
        // if suffix_i < suffix_j, it returns -1
        // if suffix_i = suffix_j, it returns 0;
        // It is not implemented yet,
        // It should be used to create suffix array.
        // Example of dictionary order
        // "i" < "o" : compare by code
        // "Hi" < "Ho" ; if head is same, compare the next element
        // "Ho" < "Ho " ; if the prefix is identical, longer string is big
        
        // 指導書より
        int si = suffixArray[i];
        int sj = suffixArray[j];
        int s = 0;
        if(si > s) s= si;
        if(sj > s) s= sj;
        int n = mySpace.length - s;
        for(int k=0;k<n;k++) {
            if(mySpace[si+k]>mySpace[sj+k]) return 1;
            if(mySpace[si+k]<mySpace[sj+k]) return -1;
        }
        // 接頭辞が等しいので、文字列の長さ（インデックス）で大小比較
        if(si < sj) return 1;
        if(si > sj) return -1;
        return 0;
    }

    //ピボットのIDを選ぶ関数
    int choose_pivod_id(int leftID, int rightID){
        return leftID;
    }

    //pivotがある場所のIDを返す関数
    int partition(int leftID, int rightID, int pivotID){//System.out.print("ここまでうごいた");

        //いったんpivotを右端に保存する
        int tmp = suffixArray[pivotID];
        suffixArray[pivotID] = suffixArray[rightID];
        suffixArray[rightID] = tmp;

        //左から見ていき,pivot以下のデータをpと入れ替える
        int p = leftID;
        for(int i=leftID; i<=rightID-1; i++){
            int tmp2;
            int flag = suffixCompare(rightID,i);
            if(flag == 1 || flag == 0){
                tmp2 = suffixArray[i];
                suffixArray[i] = suffixArray[p];
                suffixArray[p] = tmp2;
                p++;
            }
        }

        //保存しておいたpivodをpの位置へ移動
        int tmp3 = suffixArray[p];
        suffixArray[p] = suffixArray[rightID];
        suffixArray[rightID] = tmp3; 
        
        return p;

    }

    //ソート部分
    void QSort(int leftID ,int rightID){

        //終了条件
        if(leftID >= rightID){
            return;
        }

        //ピボットのIDを選ぶ
        int pivotID = choose_pivod_id(leftID, rightID);

        //pivotより大きいグループと小さいグループに分ける
        int p = partition(leftID, rightID, pivotID); 

        //左側をクイックソート
        QSort(leftID, p-1);

        //右側をクイックソート
        QSort(p+1, rightID);

    }
    
    public void setSpace(byte []space) {

        //mySpaceの生成を行う
        mySpace = space;
        if(mySpace.length>0) spaceReady = true;
        suffixArray = new int[space.length];
        // put all suffixes in suffixArray. Each suffix is expressed by one interger.
        for(int k = 0; k< space.length; k++) {
            suffixArray[k] = k;
        }
        
        //クイックソートによるソート
        QSort(0, space.length-1);

        // ここでバブルソートによるソート
        /*
        for(int i = 0; i< space.length; i++){
            for(int j = i+1; j < space.length; j++){
                int tmp;
                int flag = suffixCompare(i,j);
                if(flag == 1){
                    tmp = suffixArray[i];
                    suffixArray[i] = suffixArray[j];
                    suffixArray[j] = tmp;
                }
            }
        }
        */
            
        //printSuffixArray();
        
        /* Example from "Hi Ho Hi Ho"
         0: Hi Ho
         1: Ho
         2: Ho Hi Ho
         3:Hi Ho
         4:Hi Ho Hi Ho
         5:Ho
         6:Ho Hi Ho
         7:i Ho
         8:i Ho Hi Ho
         9:o
         A:o Hi Ho
         */
    }
    
    private int targetCompare(int i, int start, int end) {
        // comparing suffix_i and target_j_end by dictonary order with limitation of length;
        // if the beginning of suffix_i matches target_i_end, and suffix is longer than target it returns 0;
        // if suffix_i > target_start_end it return 1;
        // if suffix_i < target_start_end it return -1
        // It is not implemented yet.
        // It should be used to search the apropriate index of some suffix.
        // Example of search
        // suffix target
        // "o" > "i"
        // "o" < "z"
        // "o" = "o"
        // "o" < "oo"
        // "Ho" > "Hi"
        // "Ho" < "Hz"
        // "Ho" = "Ho"
        // "Ho" < "Ho " : "Ho " is not in the head of suffix "Ho"
        // "Ho" = "H" : "H" is in the head of suffix "Ho"
        
        int si = suffixArray[i];
        int s = 0;
        if(si > s) s = si;
        int n = mySpace.length - s;
        
        // suffix_iの文字列の長さがtargetの文字列の長さより長い場合、比較回数をtargetの長さにする
        if(n > end) n = end;
        
        // 接頭辞の大きさを比較
        for(int k=0; k<n; k++) {
            if(mySpace[si+k] > myTarget[k]) return 1;
            if(mySpace[si+k] < myTarget[k]) return -1;
        }
        
        // 接頭辞が等しいので、文字列の長さで大小比較
        int spaLeng = mySpace.length - s; //suffixの文字列の長さ
        int tarLeng = end; // targetの文字列の長さ、myTarget.length
        
        /* デバッグ用 */
        //System.out.println("Index:"+i);
        //System.out.println("suffix:"+spaLeng);
        //System.out.println("target:"+tarLeng);
        
        if(spaLeng < tarLeng) return -1; // targetの方が長い場合
        
        // 上のどの条件にも該当しなかったので、targetは接頭辞であると言える
        return 0;
    }
    
    private int subByteStartIndex(int start, int end) {
        // It returns the index of the first suffix which is equal or greater than subBytes;
        // not implemented yet;
        // For "Ho", it will return 5 for "Hi Ho Hi Ho".
        // For "Ho ", it will return 6 for "Hi Ho Hi Ho".
        
        int sl = suffixArray.length;
        int result = -1;
        
        for(int i = 0; i < sl; i++){
            result = targetCompare(i,start,end);
            // suffix_iとtargetが初めて等しくなった時のインデックスを返す
            //System.out.println("スタートコンペア"+result); //デバッグ用
            if(result == 0) return i;
        }
        // suffix_iとtargetが一度も等しくならなかった
        return suffixArray.length;
    }
    
    private int subByteEndIndex(int start, int end) {
        // It returns the next index of the first suffix which is greater than subBytes;
        // not implemented yet
        // For "Ho", it will return 7 for "Hi Ho Hi Ho".
        // For "Ho ", it will return 7 for "Hi Ho Hi Ho".
        
        int sl = suffixArray.length;
        int result = 0;
        
        for(int i = 0; i < sl; i++){
            result = targetCompare(i,start,end);
            // suffix_iが初めてtargetより大きくなった時のインデックスを返す
            //System.out.println("エンドコンペア"+result); //デバッグ用
            if(result == 1) return i;
        }
        // suffix_iが一度もtargetより大きくならなかった
        return suffixArray.length;
    }
    
    //検索に引っかかった要素の数を返す
    public int subByteFrequency(int start, int end) {
        //This method could be defined as follows though it is slow.
        int spaceLength = mySpace.length;
        int count = 0;
        for(int offset = 0; offset< spaceLength - (end - start); offset++) {
            boolean abort = false;
            for(int i = 0; i< (end - start); i++) {
                if(myTarget[start+i] != mySpace[offset+i]) { abort = true; break; }
            }
            if(abort == false) {
                count++;
            }
        }
        int first = subByteStartIndex(start,end);
        int last = subByteEndIndex(start, end);
        //inspection code
        /*
        for(int k=start;k<end;k++) {
            System.out.write(myTarget[k]);
        }
        System.out.printf(": first=%d last=%d\n", first, last);
        */
        return last - first;
    }
    
    public void setTarget(byte [] target) {
        myTarget = target;
        if(myTarget.length>0) targetReady = true;
    }
    
    //条件がそろっていたらオブジェクトを作成
    public int frequency() {
        if(targetReady == false) return -1;
        if(spaceReady == false) return 0;
        return subByteFrequency(0, myTarget.length);
    }
    
    public static void main(String[] args) {
        Frequencer frequencerObject;
        try {
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("Hi Ho Hi Ho".getBytes());
            frequencerObject.setTarget("Ho ".getBytes());
            int result = frequencerObject.frequency();
            System.out.print("Freq = "+ result+" ");
            if(4 == result) {
                System.out.println("OK");
            }else{
                System.out.println("WRONG");
            }
        }
        catch(Exception e) {
            System.out.println("STOP");
        }
    }
}

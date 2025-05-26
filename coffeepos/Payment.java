package coffeepos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class Payment {
    // 순서(1부터 n까지), {메뉴 이름, 판매 수량, 판매 갸격}} -> 이것은 영수증 내용ㅋ (주기적으로 초기화 됨)
    public static HashMap<Integer, ArrayList<String>> receiptList = new HashMap<>();
    // 현재까지 처리된 주문 내역 목록 (영수증 단위로 출력하기)
    // 날짜, {순서 {순서(1부터 n까지) {메뉴 이름, 판매 수량, 판매 가격}}} (초기화 되면 안 되는 내용)
    public static HashMap<String, ArrayList<HashMap<Integer, ArrayList<String>>>> paymentListMap = new HashMap<>();
    public static int sumOfCreditCard;    // 카드로 결제한 모든 금액 저장
    public static int sumOfCash;    // 현금으로 결제한 모든 금액 저장
    public static int sumOfDrink;   // 음료의 모든 금액 저장
    public static int sumOfDesert;  // 디저트의 모든 금액 저장

    public Payment() {

    }

    public static void paymentPrint(int sum, HashMap<String, Integer> choiceList) {
        int idx = 1;
        ArrayList<String> list = new ArrayList<>();
        System.out.println("\n=============== 결제 ===============");
        System.out.println("  * 선택한 메뉴");
        for(String name : choiceList.keySet()) {
            System.out.println("   " +name + " : " +choiceList.get(name) +"개   " +(Menu.getPrice(name) * choiceList.get(name)) +"원");
            list.add(name);
            list.add(String.valueOf(choiceList.get(name)));
            list.add(String.valueOf(Menu.getPrice(name) * choiceList.get(name)));
            receiptList.put(idx, list);
            idx++;

            // TODO 지금은 음료수, 디저트 총 가격을 여기서 더하지만 나중에 떨어뜨려놔야...
            if(Menu.drinkMenus.containsKey(name)) { // 음료 메뉴에 있는 거면
                sumOfDrink += Menu.drinkMenus.get(name).getFirst() * choiceList.get(name);  // 곱한 걸 더해줌
            }
            else if(Menu.desertMenus.containsKey(name)) {   // 디저트 메뉴에 있는 거면
                sumOfDesert += Menu.desertMenus.get(name).getFirst() * choiceList.get(name);
            }
        }
        System.out.println("\n  * 총 금액: " +sum +"\n");

        System.out.println("   (1) 결제하기    (2) 취소(홈으로)");

        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.println("   >> ");
            int choice = sc.nextInt();

            if(choice == 1) {
                payingWayPrint(sum);
                break;
            }
            else if(choice == 2) {
                Main.mainPrint();
                break;
            }
            else System.out.println("\n다시 입력해주세요:)");
        }
    }

    // 결제 방식 고르기
    public static void payingWayPrint(int sum) {
        System.out.println("\n======== 결제할 방법 ========");
        System.out.println("  (1) 카드        (2) 현금");
        System.out.println("===========================");
        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.println("   >> ");
            int choice = sc.nextInt();

            if(choice == 1) {
                creditCard(sum);
                break;
            }
            else if(choice == 2) {
                cash(sum);
                break;
            }
            else System.out.println("1과 2 중 다시 선택해주세요.");
        }
    }

    // 카드 결제
    public static void creditCard(int sum) {
        System.out.println("\n========= 카드 결제 =========");
        System.out.println("\n  * 총 결제할 금액 : " +sum);

        System.out.println("\n 결제가 완료되었습니다:)");
        sumOfCreditCard += sum;

        receipt(sum, sum);  // 카드는 딱 맞춰 넣으니까
    }

    // 현금 결제
    public static void cash(int sum) {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n========= 현금 결제 =========");
        System.out.print(" 돈을 넣어주세요!  >> ");
        int cash;
        int cashSum = 0;    // 돈을 추가로 넣어야 할 때
        while(true) {
            // 결제 금액 입력
            cash = sc.nextInt();
            cashSum += cash;

            if(cash >= sum) {
                System.out.println("\n  * 총 결제할 금액 : " +sum);
                System.out.println("  * 넣은 금액 : " +cashSum);
                System.out.println("  * 거스름돈 : " +(cash - sum));
                break;
            }
            else {
                System.out.println("돈이 부족합니다. 돈을 추가로 넣어주세요\n  >> ");
            }
        }
        sumOfCash += sum;

        System.out.println("\n 결제가 완료되었습니다:)");

        cashSum = cash;
        // 잔돈 계산 후 출력(영수증에도 같이 출력되게)
        receipt(sum, cashSum);
    }

    // 영수증 출력 후 paymentListMap에 저장해야 함 (영수증 저장하면서 결제 시간 넣기)
    public static void receipt(int sum, int cash) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = formatter.format(date);

        System.out.println("\n========== 영수증 ==========");
        for(int i = 0; i < receiptList.size(); i++) {
            System.out.println(" (" +(i + 1) +") " +receiptList.get(i).getFirst() +"  " +receiptList.get(i).get(1) +"개");   // 메뉴 이름 출력
            System.out.println("    -> " +receiptList.get(i).getLast() +"원");
        }
        System.out.println();
        System.out.println("\n  * 총 결제할 금액 : " +sum);
        System.out.println("  * 결제한 금액 : " +cash);
        System.out.println("  * 거스름돈 : " +(cash - sum));
        System.out.println("===========================");

        ArrayList<HashMap<Integer, ArrayList<String>>> lists = new ArrayList<>();
        lists.add(receiptList);

        paymentListMap.put(strDate, lists);   // 그 날짜의 영수증 넣기
    }
}
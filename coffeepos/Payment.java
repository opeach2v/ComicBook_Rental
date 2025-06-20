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
    public static HashMap<String, Integer> PriceByMenu = new HashMap<>();    // 메뉴별 가격 저장 (메뉴 이름, 팔린 총 가격)
    public static int sumOfCreditCard;    // 카드로 결제한 모든 금액 저장
    public static int sumOfCash;    // 현금으로 결제한 모든 금액 저장
    public static int sumOfDrink;   // 음료의 모든 금액 저장
    public static int sumOfDesert;  // 디저트의 모든 금액 저장

    public Payment() {

    }

    public static void initPriceByMenu() {  // 메뉴별 판매 가격 초기화 (0원으로)
        for(int idx : Menu.drinkMenus.keySet()) {
            for(String menu : Menu.drinkMenus.get(idx).keySet()) PriceByMenu.put(menu, 0);
        }
        for(int idx : Menu.desertMenus.keySet()) {
            for(String menu : Menu.desertMenus.get(idx).keySet()) PriceByMenu.put(menu, 0);
        }
    }

    public static void paymentPrint(int sum, HashMap<String, ArrayList<Integer>> choiceList) {
        int idx = 1;
        System.out.println("\n=============== 결제 ===============");
        System.out.println("  * 선택한 메뉴");
        for(String name : choiceList.keySet()) {
            ArrayList<String> list = new ArrayList<>();
            System.out.println("   " +name + "\t:  " +choiceList.get(name).get(1) +"개   " +(choiceList.get(name).get(0) * choiceList.get(name).get(1)) +"원");
            list.add(name);
            list.add(String.valueOf(choiceList.get(name).get(1)));
            list.add(String.valueOf(choiceList.get(name).get(0) * choiceList.get(name).get(1)));
            receiptList.put(idx, list);
            idx++;

            // TODO 지금은 음료수, 디저트 총 가격을 여기서 더하지만 나중에 떨어뜨려놔야...
            for(int num : Menu.drinkMenus.keySet()) {
                if(Menu.drinkMenus.get(num).containsKey(name)) { // 음료 메뉴에 있는 거면
                    sumOfDrink += (choiceList.get(name).get(0) * choiceList.get(name).get(1));  // 곱한 걸 더해줌
                    PriceByMenu.put(name, PriceByMenu.getOrDefault(name, 0) + sumOfDrink);  // 해당 메뉴의 판매 가격을 계속 업데이트
                }
                else break;
            }
            for(int num : Menu.desertMenus.keySet()) {
                if(Menu.desertMenus.get(num).containsKey(name)) {   // 디저트 메뉴에 있는 거면
                    sumOfDesert += (choiceList.get(name).get(0) * choiceList.get(name).get(1));
                    PriceByMenu.put(name, PriceByMenu.getOrDefault(name, 0) + sumOfDesert);
                }
                else break;
            }
        }
        System.out.println("\n  * 총 금액: " +sum +"\n");

        System.out.println("   (1) 결제하기    (2) 취소(홈으로)");

        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.print("   >> ");
            int choice = sc.nextInt();

            if(choice == 1) {
                payingWayPrint(sum);
                break;
            }
            else if(choice == 2) {
                Main.mainPrint();
                // TODO 다시 수량 취소 돌려 놔야 함 (아 복잡해졋어 나중에..)
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
            System.out.print("   >> ");
            int choice = sc.nextInt();

            if(choice == 1) {
                creditCard(sum);
                Main.mainPrint();
                break;
            }
            else if(choice == 2) {
                cash(sum);
                Main.mainPrint();
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

            if(cashSum >= sum) {
                System.out.println("\n  * 총 결제할 금액 : " +sum);
                System.out.println("  * 넣은 금액 : " +cashSum);
                System.out.println("  * 거스름돈 : " +(cashSum - sum));
                break;
            }
            else {
                System.out.print("돈이 부족합니다. 돈을 추가로 넣어주세요\n  >> ");
            }
        }
        sumOfCash += sum;

        System.out.println("\n 결제가 완료되었습니다:)");
        // 잔돈 계산 후 출력(영수증에도 같이 출력되게)
        receipt(sum, cashSum);
    }

    // 영수증 출력 후 paymentListMap에 저장해야 함 (영수증 저장하면서 결제 시간 넣기)
    public static void receipt(int sum, int cashSum) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = formatter.format(date);

        System.out.println("\n========== 영수증 ==========");
        for(int i = 1; i <= receiptList.size(); i++) {
            System.out.println(" (" +(i) +") " +receiptList.get(i).get(0) +"  " +receiptList.get(i).get(1) +"개");   // 메뉴 이름 출력
            System.out.println("    -> " +receiptList.get(i).get(2) +"원");
        }
        System.out.println();
        System.out.println("\n  * 총 결제할 금액 : " +sum);
        System.out.println("  * 결제한 금액 : " +cashSum);
        System.out.println("  * 거스름돈 : " +(cashSum - sum));
        System.out.println("===========================");
        System.out.println(" 구매 감사합니다~!");

        ArrayList<HashMap<Integer, ArrayList<String>>> lists = new ArrayList<>();
        lists.add(receiptList);

        paymentListMap.put(strDate, lists);   // 그 날짜의 영수증 넣기
    }
}
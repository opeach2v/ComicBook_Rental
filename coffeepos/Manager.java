package coffeepos;

import java.util.Scanner;

public class Manager {
    public Manager() {
    }

    // 관리자 메뉴
    public static void managerPrint() {
        Scanner sc = new Scanner(System.in);

        while(true) {
            System.out.println("\n======== 관리자 모드 ========");
            System.out.println("   1. 전체 주문 내역 보기");
            System.out.println("   2. 메뉴별 판매 통계 보기");
            System.out.println("   3. 기간별 판매 통계 보기");
            System.out.println("   4. 영수증 모두 보기");
            System.out.println("   5. 홈으로");
            System.out.println("============================");
            System.out.print("   >> ");
            int choice = sc.nextInt();

            if(choice == 1) {
                allSellList();
            }
            else if(choice == 2) {
                menuSellList();
            }
            else if(choice == 3) {
                dateSellList();
            }
            else if(choice == 4) {
                allReceipt();
            }
            else if(choice == 5) {
                Main.mainPrint();
                break;
            }
            else System.out.println("1 ~ 5 중 다시 선택해주세요.");
        }
    }

    // 전체 주문 내역 보기
    public static void allSellList() {
        System.out.println("=====================================\n");
        // 현금과 카드 총 주문 금액 출력
        System.out.println("    * 현금 총 금액 : " +Payment.sumOfCash +"원");
        System.out.println("    * 카드 총 금액 : " +Payment.sumOfCreditCard +"원");
        System.out.println();

        // 음료와 디저트 총 주문 금액 출력
        System.out.println("    * 음료 총 금액 : " +Payment.sumOfDrink +"원");
        System.out.println("    * 디저트 총 금액 : " +Payment.sumOfDesert +"원");
        System.out.println();

        // 진짜 총 주문 금액 출력
        int total = Payment.sumOfCash + Payment.sumOfCreditCard + Payment.sumOfDrink + Payment.sumOfDesert;
        System.out.println("      * 총 금액 : " +total +"원");
        System.out.println();
    }

    // TODO 메뉴별 판매 통계 보기
    public static void menuSellList() {
        Scanner sc = new Scanner(System.in);
        if(Payment.paymentListMap.isEmpty()) {
            System.out.println("아직 판매한 기록이 없습니다.\n");
            return;
        }
        int total = 0;

        System.out.println("    [ 메뉴별 통계 보기 ]     ");
        for(String name : Payment.PriceByMenu.keySet()) {
            System.out.println("  " + name + " : " + Payment.PriceByMenu.get(name));
            total += Payment.PriceByMenu.get(name);
        }
        System.out.println("  +모든 메뉴의 총 합 : " +total);
    }

    // TODO 기간별 판매 통계 보기
    public static void dateSellList() {
        Scanner sc = new Scanner(System.in);
        if(Payment.paymentListMap.isEmpty()) {
            System.out.println("아직 판매한 기록이 없습니다.\n");  // 비어있을 시
            return;
        }
        int sum;
        int total = 0;

        System.out.println("\n========= 기간 선택 =========");
        for(String s : Payment.paymentListMap.keySet()) {   // 날짜 출력
            System.out.println("   " +s);
        }
        while (true) {
            System.out.print("통계가 보고 싶은 기간을 적으세요 >> ");
            String choice = sc.next();
            if(Payment.paymentListMap.containsKey(choice)) {  // 해당 날짜가 존재하면
                for(int i = 0; i < Payment.paymentListMap.get(choice).size(); i++) {
                    for(int n : Payment.paymentListMap.get(choice).get(i).keySet()) {
                        System.out.println("\n [ " +i +"번째 결제 내역 ]");
                        System.out.println("\t" +Payment.paymentListMap.get(choice).get(i).get(n).get(0) +"  "
                                + Payment.paymentListMap.get(choice).get(i).get(n).get(1) +"개  "
                                +Payment.paymentListMap.get(choice).get(i).get(n).get(2) +"원");  // 출력
                        sum = Integer.parseInt(Payment.paymentListMap.get(choice).get(i).get(n).get(1))
                                * Integer.parseInt(Payment.paymentListMap.get(choice).get(i).get(n).get(2));
                        total += sum;
                    }
                }
                System.out.println();
                System.out.println("           => 결제한 금액 : " +total +"원");
            }
            else {
                System.out.println("존재하지 않는 기간입니다.\n다시 확인해서 정확하게 적어주세요.");
            }
        }
    }

    // 영수증 모두 보기
    // TODO 카드로 계산했는지 현금으로 계산 했는지도 저장하기
    public static void allReceipt() {
        int total = 0;  // 전체 결제 금액
        int sum = 0;    // 한 메뉴의 총 결제 금액

        if(!Payment.paymentListMap.isEmpty()) {
            for(String s : Payment.paymentListMap.keySet()) {   // 날짜 출력
                System.out.println("┌────────────────────────────────────────┐");
                System.out.println("   " +s);
                System.out.println("                영  수  증");
                System.out.println();
                for(int i = 0; i < Payment.paymentListMap.get(s).size(); i++) { // 해당 기간의 첫 번째 결제부터
                    for(int n : Payment.paymentListMap.get(s).get(i).keySet()) {
                        System.out.println("\t" +Payment.paymentListMap.get(s).get(i).get(n).get(0) +"  "
                                + Payment.paymentListMap.get(s).get(i).get(n).get(1) +"개  "
                                +Payment.paymentListMap.get(s).get(i).get(n).get(2) +"원");  // 출력
                        sum = Integer.parseInt(Payment.paymentListMap.get(s).get(i).get(n).get(1))
                                * Integer.parseInt(Payment.paymentListMap.get(s).get(i).get(n).get(2));
                        total += sum;
                    }
                }
                System.out.println();
                System.out.println("           * 결제한 금액 : " +total +"원");
                System.out.println();
                System.out.println("└────────────────────────────────────────┘\n");
            }
        }
        else {
            System.out.println("아직 판매한 기록이 없습니다.\n");
            return;
        }
    }
}
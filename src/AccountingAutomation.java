import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class AccountingAutomation
{

    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        String userChoice;
        System.out.println("Выберите нужное действие: \n");
        System.out.println("""
                1 - Считать все месячные отчёты \s
                2 - Считать все годовые отчёты \s
                3 - Сверить отчёты  \s
                4 - Вывести информацию о всех месячных отчётах  \s
                5 - Вывести информацию о годовом отчёте \s
                Пустота и ENTER - выход из программы""");
        do
        {
            userChoice = sc.nextLine();
            switch (userChoice)
            {
                case "1" ->
                {
                    System.out.println("Считывание месячных отчётов: ");
                    System.out.println("Товар, трата(да/нет), количество, цена\n");
                    ArrayList<MonthlyReport> monthsLines;
                    for (int j = 1; j <= 3; j++) {
                        String monthNumber = (j < 10 ? "0" : "") + j;
                        String path = "C:\\!study\\!proga\\AccountingAutomation\\csv\\m.2021" + monthNumber + ".csv";

                        monthsLines = readMonthlyReport(Objects.requireNonNull(readFileContentsOrNull(path)));
                        assert monthsLines != null;
                        for (MonthlyReport report : monthsLines)
                        {
                            System.out.println(report.toString());
                        }
                    }
                }
                case "2" -> {
                    System.out.println("Считывание годовых отчётов: ");
                    System.out.println("Месяц, Сумма, Трата(да/нет)\n");
                    ArrayList<YearlyReport> yearlyReport = readYearlyReport(Objects.requireNonNull(readFileContentsOrNull("C:\\!study\\!proga\\AccountingAutomation\\csv\\y.2021.csv")));
                    for (YearlyReport report : yearlyReport) {
                        System.out.println(report.toString());
                    }
                }
                case "3" ->
                {
                    System.out.println("Сверка отчётов: ");
                    ArrayList<MonthlyReport> monthsReports;
                    Map<Integer, ArrayList<MonthlyReport>> months = new HashMap<>();
                    for (int i = 1; i < 4; i++)
                    {
                        String monthNumber = (i < 10 ? "0" : "") + i;
                        String path = "C:\\!study\\!proga\\AccountingAutomation\\csv\\m.2021" + monthNumber + ".csv";
                        monthsReports = readMonthlyReport(Objects.requireNonNull(readFileContentsOrNull(path)));
                        months.put(i, monthsReports);
                    }
                    String path = "C:\\!study\\!proga\\AccountingAutomation\\csv\\y.2021.csv";
                    ArrayList<YearlyReport> yearlyReport = readYearlyReport(Objects.requireNonNull(readFileContentsOrNull(path)));
                    verifyReports(months, yearlyReport, path);
                }
                case "4" ->
                {
                    System.out.println("Информация о месячных отчётах: ");
                    ArrayList<MonthlyReport> monthsLines;
                    for (int i = 1; i < 4; i++)
                    {
                        String monthNumber = (i < 10 ? "0" : "") + i;
                        String path = "C:\\!study\\!proga\\AccountingAutomation\\csv\\m.2021" + monthNumber + ".csv";
                        monthsLines = readMonthlyReport(Objects.requireNonNull(readFileContentsOrNull(path)));
                        monthlyReportInformation(monthsLines, i);
                    }

                }
                case "5" -> {
                    System.out.println("Информация о годовом отчёте: ");
                    String path = "C:\\!study\\!proga\\AccountingAutomation\\csv\\y.2021.csv";
                    ArrayList<YearlyReport> report = readYearlyReport(readFileContentsOrNull(path));
                    yearlyReportInformation(report, path);
                }
            }
        } while (!userChoice.isEmpty());
        System.out.println("Действие программы завершено");
    }

    private static void yearlyReportInformation(ArrayList<YearlyReport> report, String path)
    {
        String year = path.substring(path.lastIndexOf("y.") + 2, path.lastIndexOf(".csv"));
        System.out.println("Год: " + year);

        Map<Integer, Integer> monthlyProfit = new HashMap<>();
        int totalExpense = 0;
        int totalIncome = 0;
        int expenseMonths = 0;
        int incomeMonths = 0;

        for (YearlyReport entry : report)
        {
            int month = entry.getMonth();
            int amount = entry.getAmount();

            if (entry.isIs_expense())
            {
                totalExpense += amount;
                expenseMonths++;
                monthlyProfit.put(month, monthlyProfit.getOrDefault(month, 0) - amount);
            } else
            {
                totalIncome += amount;
                incomeMonths++;
                monthlyProfit.put(month, monthlyProfit.getOrDefault(month, 0) + amount);
            }
        }

        for (Map.Entry<Integer, Integer> entry : monthlyProfit.entrySet())
        {
            System.out.println("Месяц: " + entry.getKey() + ", Прибыль: " + entry.getValue());
        }

        double averageExpense = expenseMonths > 0 ? (double) totalExpense / expenseMonths : 0;
        double averageIncome = incomeMonths > 0 ? (double) totalIncome / incomeMonths : 0;

        System.out.println("Средний расход за все месяцы: " + averageExpense);
        System.out.println("Средний доход за все месяцы: " + averageIncome);
    }

    private static void monthlyReportInformation(ArrayList<MonthlyReport> report, int n)
    {
        String monthName;
        switch (n) {
            case 1 -> monthName = "Январь";
            case 2 -> monthName = "Февраль";
            case 3 -> monthName = "Март";
            case 4 -> monthName = "Апрель";
            case 5 -> monthName = "Май";
            case 6 -> monthName = "Июнь";
            case 7 -> monthName = "Июль";
            case 8 -> monthName = "Август";
            case 9 -> monthName = "Сентябрь";
            case 10 -> monthName = "Октябрь";
            case 11 -> monthName = "Ноябрь";
            case 12 -> monthName = "Декабрь";
            default ->
            {
                System.out.println("Некорректный номер месяца.");
                return;
            }
        }

        MonthlyReport maxExpense = null;
        MonthlyReport maxPurchase = null;

        for (MonthlyReport item : report)
        {
            int total = item.getQuantity() * item.getSum_of_one();
            if (item.isIs_expense())
            {
                if (maxExpense == null || total > maxExpense.getQuantity() * maxExpense.getSum_of_one())
                {
                    maxExpense = item;
                }
            } else
            {
                if (maxPurchase == null || total > maxPurchase.getQuantity() * maxPurchase.getSum_of_one())
                {
                    maxPurchase = item;
                }
            }
        }

        System.out.println("Месяц: " + monthName);
        if (maxExpense != null)
        {
            System.out.println("Самая большая трата: " + maxExpense.getItem_name() + ", сумма: " + (maxExpense.getQuantity() * maxExpense.getSum_of_one()));
        } else
        {
            System.out.println("Нет данных о тратах.");
        }
        if (maxPurchase != null)
        {
            System.out.println("Самая большая прибыль: " + maxPurchase.getItem_name() + ", сумма: " + (maxPurchase.getQuantity() * maxPurchase.getSum_of_one()));
        } else
        {
            System.out.println("Нет данных о прибыли.");
        }
    }
    private static void verifyReports(Map<Integer, ArrayList<MonthlyReport>> monthlyReports, ArrayList<YearlyReport> yearlyReports, String path)
    {
        String yearNumber = path.substring(44,48);
        Map<Integer, Integer> monthlyIncome = new HashMap<>();
        Map<Integer, Integer> monthlyExpense = new HashMap<>();
        System.out.println("Рассматриваемый год: " + yearNumber);
        for (Map.Entry<Integer, ArrayList<MonthlyReport>> entry : monthlyReports.entrySet())
        {
            int month = entry.getKey();
            int totalIncome = 0;
            int totalExpense = 0;

            for (MonthlyReport report : entry.getValue())
            {
                int total = report.getQuantity() * report.getSum_of_one();
                if (report.isIs_expense())
                {
                    totalExpense += total;
                } else
                {
                    totalIncome += total;
                }
            }

            monthlyIncome.put(month, totalIncome);
            monthlyExpense.put(month, totalExpense);
        }
        for (YearlyReport yearlyReport : yearlyReports)
        {
            int month = yearlyReport.getMonth();
            int amount = yearlyReport.getAmount();
            boolean isExpense = yearlyReport.isIs_expense();
            if (isExpense)
            {
                if (!monthlyExpense.containsKey(month) || monthlyExpense.get(month) != amount)
                {
                    System.out.println("Несоответствие расходов в месяце " + month + ": годовой отчет = " + amount + ", месячный отчет = " + monthlyExpense.getOrDefault(month, 0));
                }
            } else
            {
                if (!monthlyIncome.containsKey(month) || monthlyIncome.get(month) != amount)
                {
                    System.out.println("Несоответствие доходов в месяце " + month + ": годовой отчет = " + amount + ", месячный отчет = " + monthlyIncome.getOrDefault(month, 0));
                }
            }
        }
    }
    private static ArrayList<YearlyReport> readYearlyReport(String readedFile)
    {
        ArrayList<YearlyReport> yearlyReport = new ArrayList<>();
        if (readedFile == null || readedFile.isEmpty())
        {
            System.out.println("Файл пустой или не найден.");
            return yearlyReport;
        }

        String[] lines = readedFile.split("\n");
        for (int i = 1; i < lines.length; i++)
        {
            String line = lines[i].trim();
            if (line.isEmpty())
            {
                continue;
            }

            String[] parts = line.split(",");
            if (parts.length != 3)
            {
                System.out.println("Некорректное количество данных в строке: " + (i + 1));
                continue;
            }

            try
            {
                YearlyReport report = new YearlyReport();
                report.setMonth(Byte.parseByte(parts[0].trim()));
                report.setAmount(Integer.parseInt(parts[1].trim()));
                report.setIs_expense(Boolean.parseBoolean(parts[2].trim().toLowerCase()));
                yearlyReport.add(report);
            } catch (NumberFormatException e)
            {
                System.out.println("Ошибка преобразования данных в строке: " + (i + 1));
            }
        }
        return yearlyReport;
    }

    private static ArrayList<MonthlyReport> readMonthlyReport (String readedFile)
    {        String[] lines = readedFile.split("\n");
        ArrayList<MonthlyReport> monthlyReports = new ArrayList<>();

        for (int i = 1; i < lines.length; i++)
        {
            MonthlyReport report = new MonthlyReport();
            String[] line = lines[i].split(",");
            if (line.length>4)
            {
                System.out.println("В строке больше данных, чем нужно. Строка: " + i+1);
                return null;
            }
            report.setItem_name(line[0]);
            if (line[1].equalsIgnoreCase("TRUE"))
            {
                report.setIs_expense(true);
            } else if (line[1].equalsIgnoreCase("FALSE"))
            {
                report.setIs_expense(false);
            } else
            {
                System.out.println("Неверное значение логической переменной в строке: " + (i + 1));
                continue;
            }
            report.setQuantity(Integer.parseInt(line[2]));
            report.setSum_of_one(Integer.parseInt(line[3]));
            monthlyReports.add(report);

        }
        return monthlyReports;
    }

    private static String readFileContentsOrNull(String path)
    {
        try
        {
            return Files.readString(Path.of(path));
        }
        catch (IOException e)
        {
            System.out.println("Невозможно прочитать файл с отчётом");
            return null;
        }
    }
}
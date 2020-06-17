import com.sun.javafx.image.IntPixelGetter;
import javafx.util.Pair;

import java.util.*;

class Process implements Comparable<Process>{


    String name;
    public Integer priority,burstTime, arrivalTime, waitingTime;
    public double quantum;

    Process(String nm, Integer bt, Integer at, Integer p ,Integer qt){
        name = nm;
        priority = p;
        burstTime = bt;
        arrivalTime = at;
        quantum = qt;
        waitingTime = 0;
    }

    @Override
    public int compareTo(Process process) {
        int compareArrival = process.arrivalTime;
        return this.arrivalTime - compareArrival;
    }
}

public class Main {
    static void SJF(Process process[], Integer n){

        Integer time = 0, min = 100005, complete = 0, proc = 0;
        boolean check = false;
        Integer wt[] = new Integer[n];
        Integer turnAround[] = new Integer[n];
        Integer remainTime[] = new Integer[n];
        Integer context = 2, arrival =100005, start = 0;
        String pro = null;
        
        for (int i=0; i<n; i++) {
            remainTime[i] = process[i].burstTime;
            if(process[i].arrivalTime<arrival)
            {
                arrival = process[i].arrivalTime;
                proc = i;
                pro = process[i].name;
            }
        }

        while (complete!=n){
            for(int i=0; i<n; i++){
                if(process[i].arrivalTime<=time && remainTime[i]>0 && remainTime[i]<=min){

                   proc = i;
                   min = remainTime[i];
                   check = true;

                }
            }
            if(process[proc].name != pro) {
                time += context;

                System.out.println(pro + "              " +
                        start + "                " + (time-context));
                pro = process[proc].name;

                start = time;
            }
            if(process[proc].burstTime == remainTime[proc] && check){
                start = time;
            }

            if(check) {

                remainTime[proc]--;
                min = remainTime[proc];

                if (min <= 0)
                    min = 100005;

                if (remainTime[proc] == 0) {
                    complete++;
                    check = false;

                    wt[proc] = time-process[proc].burstTime-process[proc].arrivalTime;
                    turnAround[proc] =  wt[proc] +process[proc].burstTime;

                }
            }
            time++;
        }
        System.out.println(pro + "              " +
                start + "                " + time);


        System.out.println("Process Name    Waiting Time    Trun Around");

        for(int i=0; i<n; i++) {
            System.out.println(process[i].name + "              " +
                    wt[i] + "                " + turnAround[i]);
        }
        for(int i=1; i<n; i++)
            wt[i] += wt[i - 1];

        for(int i=1; i<n; i++)
            turnAround[i] += turnAround[i-1];

            System.out.println("Average waiting Time: " + (double)wt[n-1]/n);

        System.out.println("Average turn around Time: " + (double)turnAround[n-1]/n);

    }

    static void RR(Process process[], Integer n, Integer quantum) {
        int rem_bt[] = new int[n];
        int rem_at[] = new int[n];
        int wtt[] = new int[n];
        int timer = 0;
        int tat[] = new int[n];
        int total_wt = 0, total_tat = 0;
        String sequence = new String();

        for (int i = 0; i < n; i++) {
            rem_bt[i] = process[i].burstTime;
        }
        for (int i = 0; i < n; i++) {

            rem_at[i] = process[i].arrivalTime;
        }

        while (true) {
            boolean done = true;
            for (int i = 0; i < n; i++) {

                if (rem_at[i] <= timer) {

                    if (rem_at[i] <= quantum) {

                        if (rem_bt[i] > 0) {
                            done = false;
                            if (rem_bt[i] > quantum) {
                                timer += quantum; //how much time the process has been processed
                                rem_bt[i] -= quantum;
                                rem_at[i] += quantum;
                                sequence += " -> " + process[i].name;

                            } else //rem_bt[i]<=quantum
                            {
                                timer = timer + rem_bt[i];
                                wtt[i] = timer - process[i].burstTime - process[i].arrivalTime;
                                rem_bt[i] = 0;
                                sequence += " -> " + process[i].name;
                            }
                        }

                    } else if (rem_at[i] > quantum) {
                        for (int j = 0; j < n; j++) {

                            if (rem_at[j] < rem_at[i]) {
                                if (rem_bt[j] > 0) {
                                    done = false;
                                    if (rem_bt[j] > quantum) {
                                        timer = timer + quantum;
                                        rem_bt[j] = rem_bt[j] - quantum;
                                        rem_at[j] = rem_at[j] + quantum;
                                        sequence += " -> " + process[j].name;
                                    } else {
                                        timer = timer + rem_bt[j];

                                        wtt[j] = timer - process[j].burstTime - process[j].arrivalTime;
                                        rem_bt[j] = 0;
                                        sequence += " -> " + process[j].name;
                                    }
                                }
                            }
                        }
                        if (rem_bt[i] > 0) {
                            done = false;

                            // Check for greaters
                            if (rem_bt[i] > quantum) {
                                timer = timer + quantum;
                                rem_bt[i] = rem_bt[i] - quantum;
                                rem_at[i] = rem_at[i] + quantum;
                                sequence += " -> " + process[i].name;
                            } else {
                                timer = timer + rem_bt[i];

                                wtt[i] = timer - process[i].burstTime - process[i].arrivalTime;
                                rem_bt[i] = 0;
                                sequence += " -> " + process[i].name;
                            }
                        }

                    }

                } else if (rem_at[i] > timer) {
                    timer++;
                    i--;
                }
            }
            if (done == true) {
                break;
            }
        }
        // calculating turnaround time by adding
        // bt[i] + wt[i]
        for (int i = 0; i < n; i++) {
            tat[i] = process[i].burstTime + wtt[i];
        }

        //calculate waiting time and
        for (int i = 0; i < n; i++) {
            total_wt = total_wt + wtt[i];
            total_tat = total_tat + tat[i];
            System.out.println("process " + (i + 1) + "  burst time " + process[i].burstTime + "  waiting time " + wtt[i] + "  turnaround time " + tat[i]);
        }
        System.out.println("Average waiting time = "
                + (float) total_wt / (float) n);
        System.out.println("Average turn around time = "
                + (float) total_tat / (float) n);
        System.out.println("Processes execution order " + sequence);
    }

    public static void priority (Process Proc[], int NumbofProc){

        //SORTING DEPENDS ON ARRIVAL TIME
        for (int i = 1; i < NumbofProc; i++) {
            int temp = Proc[i].arrivalTime;
            for (int j = i; j > 0 && temp < Proc[j - 1].arrivalTime; j--) {
                //SWAP 2 objects
                Process temp1 = Proc[j];
                Proc[j] = Proc[j - 1];
                Proc[j - 1] = temp1;

            }
        }
        int[] TurnArroundTime = new int[NumbofProc];
        for (int i = 0; i < NumbofProc; i++) {
            TurnArroundTime[i] = Proc[i].burstTime;
        }

        int CurrentTime = 0; //Timeline
        int CurrentIndex = 0;// Max index = NumbofProc-1
        ArrayList<Process> alist = new ArrayList<>();


        while (!alist.isEmpty() || CurrentIndex < NumbofProc) {
            //To prevent out of bound index
            if (CurrentIndex < NumbofProc) {
                for (int i = CurrentIndex; i < NumbofProc; i++) {
                    //Condition if 2 or more proccess has the same arrival time
                    if (Proc[CurrentIndex].arrivalTime <= CurrentTime) {

                        alist.add(Proc[i]); // Adding to list the process
                        CurrentIndex++;

                    }
                }
            }

//SORTING DEPENDS ON PRIORITY
            Collections.sort(alist, (Process c1, Process c2) -> {
                if (c1.priority < c2.priority) {
                    return -1;
                }
                if (c1.priority > c2.priority) {
                    return 1;
                }
                return 0;
            });
            //Reduce Burst time each sec
            //If condition to prevent crash if there is no job to do
            if (!alist.isEmpty()) {
                alist.get(0).burstTime--;

                System.out.println("Sec:" + CurrentTime + "->" + alist.get(0).name);
            } else {
                System.out.println("Sec:" + CurrentTime + "->" + "No Job to do");
            }

            CurrentTime++;  //TimeLine
            //To Calculate waiting time
            for (int i = 1; i < alist.size(); i++) {

                alist.get(i).waitingTime++;
                //Starvation problem
                alist.get(i).priority--;

            }

            //To prevent crash if there is no job to do
            if (!alist.isEmpty()) {
                //Remove from list if finished
                if (alist.get(0).burstTime == 0) {
                    alist.remove(0);
                }

            }
        }
        double TotalWaitingTime = 0;
        double TotalAroundTime = 0;
        for (int i = 0; i < NumbofProc; i++) {
            TurnArroundTime[i] += Proc[i].waitingTime;
            System.out.println(Proc[i].name + " has " + Proc[i].waitingTime + " secs waiting time. ");
            System.out.println(Proc[i].name + " has " + TurnArroundTime[i] + " secs turn around time. ");
            TotalWaitingTime += Proc[i].waitingTime;
            TotalAroundTime += TurnArroundTime[i];

        }
        System.out.println("Average waiting time = " + (TotalWaitingTime / NumbofProc) + " secs");
        System.out.println("Average around time = " + (TotalAroundTime / NumbofProc) + " secs");

    }

    static void AG(Process process[], Integer n) {
        Arrays.sort(process);
        Integer time = 0;
        ArrayList<Pair<String,Integer>> quantumOrder = new ArrayList<>();
        Integer wt[] = new Integer[n];
        Integer turnAround[] = new Integer[n];

        //name, start, end: vector fields
        Vector<Pair<Pair<Integer, Integer>, Integer>> order = new Vector<>(); //store execution order for each process

        Integer remainTime[] = new Integer[n];
        for (int i = 0; i < n; i++)
            remainTime[i] = process[i].burstTime;

        ArrayList<Process> list = new ArrayList<>(n);
        Integer index = 0, complete = 0;

        quantumOrder.add(new Pair<String, Integer>(process[index].name,(int)process[index].quantum));
        while (complete < n) {
            Integer start = time;
            while (process[index].arrivalTime > time) {
                time++;
                start++;
            }

            int q25 = (int) Math.ceil(process[index].quantum / 4);
            Integer q50 = (int) Math.ceil(process[index].quantum / 2);
            Integer temp = index;

            Pair p1 = new Pair(process[index].name, start);
            Pair p2 = new Pair(p1, start + q25);

            order.add(p2);
            //fsfc
            remainTime[index] -= q25;
            time += q25;

            start = time;

            if (remainTime[index] <= 0) {
                complete++;
                wt[index] = time - process[index].burstTime - process[index].arrivalTime;
                turnAround[index] = wt[index] + process[index].burstTime;

                process[index].quantum = 0;
                quantumOrder.add(new Pair<String, Integer>(process[index].name,(int)process[index].quantum));

                if (remainTime[index] < 0) {
                    time += remainTime[index];

                    wt[index] += remainTime[index];
                    turnAround[index] += remainTime[index];

                    remainTime[index] = 0;
                }
                if (!list.isEmpty()) {
                    Process p = list.get(0);
                    for (int i = 0; i < n; i++) {
                        if (p == process[i]) {
                            index = i;
                            break;
                        }
                    }
                    list.remove(process[index]);
                } else {
                    for (int i = 0; i < n; i++) {
                        if (process[i].quantum != 0) {
                            index = i;
                            break;
                        }
                    }
                }
                continue;
            }


            //get next higher priority process
            for (int j = 0; j < n; j++) {
                if (process[j].arrivalTime <= time && process[j].quantum != 0 && process[j].priority < process[index].priority)
                    index = j;

            }
            list.remove(process[index]);
            //process hasn't finished push it in the stack
            //another higher priority process is executing -> start fcfs again
            if (temp != index && process[temp].quantum != 0) {
                list.add(process[temp]);//if quantum!=0 add
                process[temp].quantum += (int) Math.ceil((process[temp].quantum - q25) / 2);

                quantumOrder.add(new Pair<String, Integer>(process[temp].name,(int)process[temp].quantum));
                continue;
            }

            start = time;
            p1 = new Pair(process[index].name, start);
            p2 = new Pair(p1, start + q25);

            order.add(p2);

            //priority
            remainTime[index] -= q25;

            time += q25;
            start = time;
            if (remainTime[index] <= 0) {
                complete++;

                wt[index] = time - process[index].burstTime - process[index].arrivalTime;
                turnAround[index] = wt[index] + process[index].burstTime;

                process[index].quantum = 0;

                quantumOrder.add(new Pair<String, Integer>(process[index].name,(int)process[index].quantum));
                if (remainTime[index] < 0) {
                    time += remainTime[index];

                    wt[index] += remainTime[index];
                    turnAround[index] +=  remainTime[index];

                    remainTime[index] = 0;
                }

                if (!list.isEmpty()) {
                    Process p = list.get(0);

                    for (int i = 0; i < n; i++) {
                        if (p == process[i]) {
                            index = i;
                            break;
                        }
                    }
                    list.remove(process[index]);

                } else {
                    for (int i = 0; i < n; i++) {
                        if (process[i].quantum != 0) {
                            index = i;
                            break;
                        }
                    }
                }
                continue;
            }
            //get next sj process
            for (int j = 0; j < n; j++)
                if (process[j].arrivalTime <= time && process[j].quantum != 0 && remainTime[j] < remainTime[index])
                    index = j;

            list.remove(process[index]);
            //process hasn't finished push it in the stack if !sj
            if (temp != index && process[temp].quantum != 0) {

                list.add(process[temp]);
                process[temp].quantum += (int) Math.ceil(process[temp].quantum - q50); //q50==2*q25

                quantumOrder.add(new Pair<String, Integer>(process[temp].name,(int)process[temp].quantum));
                continue;
            }

            start = time;
            //get next sj process every second
            for (int t = 0; t < q50; t++, time++) {
                if (remainTime[index] == 0)
                    break;
                for (int j = 0; j < n; j++)
                    if (process[j].arrivalTime <= time && process[j].quantum != 0 && remainTime[j] < remainTime[index])
                        index = j;

                //another sj process is executing -> start fcfs again
                if (temp != index && process[temp].quantum != 0)
                    break;
                //if break stop this for loop and execute the while loop again

                //continue with the same process for another second
                remainTime[index]--;
                //time ++;

            }

            list.remove(process[index]);
            //broke from the for loop
            if (temp != index && process[temp].quantum != 0) {
                p1 = new Pair(process[index].name, start);
                p2 = new Pair(p1, time);

                order.add(p2);

                list.add(process[temp]);
                process[temp].quantum += (int) Math.ceil(process[temp].quantum - q25);

                quantumOrder.add(new Pair<String, Integer>(process[temp].name,(int)process[temp].quantum));
                continue;

            }

            if (remainTime[index] > 0) {
                p1 = new Pair(process[index].name, start);
                p2 = new Pair(p1, time);

                order.add(p2);

                list.add(process[index]);
                process[index].quantum += 2;

                quantumOrder.add(new Pair<String, Integer>(process[index].name,(int)process[index].quantum));
            } else if (remainTime[index] <= 0 && process[index].quantum != 0) {
                complete++;

                wt[index] = time - process[index].burstTime - process[index].arrivalTime;
                turnAround[index] = wt[index] + process[index].burstTime;

                process[index].quantum = 0;

                quantumOrder.add(new Pair<String, Integer>(process[index].name,(int)process[index].quantum));

                if (remainTime[index] < 0) {
                    time += remainTime[index];

                    wt[index] += remainTime[index];
                    //turnAround[index] =  wt[index] +process[index].burstTime;

                    remainTime[index] = 0;
                }

                if (!list.isEmpty()) {
                    //get first element inserted in the list
                    Process p = list.get(0); //aw size of list-1
                    for (int i = 0; i < n; i++) {
                        if (p == process[i]) {
                            index = i;
                            break;
                        }
                    }
                    list.remove(process[index]);
                } else {
                    for (int i = 0; i < n; i++) {
                        if (process[i].quantum != 0) {
                            index = i;
                            break;
                        }
                    }
                }
            }
        }

        System.out.println("Process"+ "    " + "start"+"     "+ "end");
        for (int i = 0; i < order.size(); i++) {
            System.out.println(order.get(i));
        }

        System.out.println("process"+"      Quantum");
        for (int i = 0; i < quantumOrder.size(); i++) {
            System.out.println(quantumOrder.get(i).getKey()+ "    " + quantumOrder.get(i).getValue());
        }

        System.out.println("Process"+ "    " + "waiting time"+"     "+ "turn around time");

        for (int i = 0; i < n; i++) {
            System.out.println(process[i].name + "    " + wt[i] + "   " + turnAround[i]);
        }


        for(int i=1; i<n; i++)
            wt[i] += wt[i - 1];

        for(int i=1; i<n; i++)
            turnAround[i] += turnAround[i-1];

        System.out.println("Average waiting Time: " + (double)wt[n-1]/n);

        System.out.println("Average turn around Time: " + (double)turnAround[n-1]/n);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String name;
        Integer priority, quantum,bt, at, number, size;


        System.out.println("Enter Number Of Processes");
        size = sc.nextInt();
        Process process[] = new Process[size];

        System.out.println("For SJF Enter 1\nFor RR Enter 2\nFor Priority Enter 3\nFor AG Enter 4");
        number = sc.nextInt();

        for(int i=0; i<size; i++) {
            System.out.println("Enter Process Name, Burst Time, Arrival Time, priority and quantum");

            name = sc.next();
            bt = sc.nextInt();
            at = sc.nextInt();
            priority = sc.nextInt();
            quantum = sc.nextInt();

            process[i] = new Process(name,bt,at,priority,quantum);
        }
        System.out.println("Process Name    Start Time    End Time");

        if(number == 1)
            SJF(process,size);

        else if(number==2) {
            System.out.println("Enter total quantum :");
            Integer totalQuantum = sc.nextInt();
            RR(process, size, totalQuantum);
        }
        else if(number==3)
            priority(process,size);
        else if(number==4){
            AG(process,size);
        }

    }

}

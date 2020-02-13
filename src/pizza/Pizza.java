/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pizza;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearNumExpr;
import ilog.cplex.IloCplex;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author rmachado
 */
public class Pizza {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, IloException {
        String filePath = "e_also_big.in";
        File file = new File(filePath);

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st = br.readLine();
        Integer M = Integer.parseInt(st.split(" ")[0]); //the maximum number of pizza slices to order
        Integer N = Integer.parseInt(st.split(" ")[1]); //the number of different types of pizza
        
        st = br.readLine();
        Integer[] S = new Integer[st.split(" ").length]; //the number of slices in each type of pizza, in non-decreasing order
        for (int i = 0; i < st.split(" ").length; i++) {
            S[i] = Integer.parseInt(st.split(" ")[i]);
        }
        
        br.close();
        IloCplex model = new IloCplex();
        
        //Var
        IloIntVar[] x = new IloIntVar[N];
        for (int i = 0; i < N; i++) {
            x[i] = model.intVar(0, 1);
        }
        
        //Obj
        IloLinearNumExpr obj = model.linearNumExpr();
        for (int i = 0; i < N; i++) {
            obj.addTerm(S[i], x[i]);
        }
        model.addMaximize(obj);
        
        //Rest
        IloLinearNumExpr rest = model.linearNumExpr();
        for (int i = 0; i < N; i++) {
            rest.addTerm(S[i], x[i]);
        }
        model.addLe(rest, M);
        
        //solve model
        if (model.solve()) {
            System.out.println("M = " + M);
            System.out.println("obj = " + model.getObjValue());
        } else {
            System.out.println("Model not solved");
        }
        model.end();
    }
    
}

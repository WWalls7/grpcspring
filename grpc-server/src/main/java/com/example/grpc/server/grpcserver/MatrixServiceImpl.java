package com.example.grpc.server.grpcserver;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import java.util.*;

@GrpcService
public class MatrixServiceImpl extends MatrixServiceGrpc.MatrixServiceImplBase {
    @Override
    public void multiplyBlock(MultiplyRequest request, StreamObserver<MultiplyReply> reply){
        System.out.println("Request recieved from client:\n" + request);
        int[][] A = arrToMat(request.getAList());
        int[][] B = arrToMat(request.getBList());
        int size = A.length;
        int[][] C = new int[size][size];

        for(int i=0; i<size; i++){ 
            for(int j=0; j<size; j++){
                for(int k=0; k<size; k++){
                    C[i][j]+=(A[i][k]*B[k][j]);
                }
            }
        }
        MultiplyReply response=MultiplyReply.newBuilder().addAllC(matToArr(C)).build();
        reply.onNext(response);
        reply.onCompleted();
    }

    @Override
    public void addBlock(AddRequest request, StreamObserver<AddReply> reply){
        System.out.println("Request recieved from client:\n" + request);
        int[][] A = arrToMat(request.getAList());
        int[][] B = arrToMat(request.getBList());
        int size = A.length;
        int[][] C = new int[size][size];
        for (int i=0; i<C.length; i++){
            for (int j=0;j<C.length;j++){
                C[i][j] = A[i][j] + B[i][j];
            }
        }
        AddReply response=AddReply.newBuilder().addAllC(matToArr(C)).build();
        reply.onNext(response);
        reply.onCompleted();
    }

    public static List<Integer> matToArr(int[][] M){
        List<Integer> array = new ArrayList<>();
        for (int i=0; i<M.length; i++)
    	{
    		for (int j=0; j<M[i].length;j++)
    		{
                array.add(M[i][j]);
    		}
        }
        return array;
    }
    
    public static int[][] arrToMat(List<Integer> A){
        int size = (int) Math.round(Math.sqrt(A.size()));
        int[][] matrix = new int[size][size];
        int count = 0;
        for (int i=0; i<size; i++)
        {
            for (int j=0; j<size;j++)
            {
                matrix[i][j] = A.get(count);
                count++;
            }
        }
        return matrix;
    }  
}
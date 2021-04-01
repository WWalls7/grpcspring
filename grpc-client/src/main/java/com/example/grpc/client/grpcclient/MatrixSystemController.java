package com.example.grpc.client.grpcclient;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.IOException;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.grpc.client.grpcclient.storage.StorageService;
import com.example.grpc.server.grpcserver.AddRequest;
import com.example.grpc.server.grpcserver.AddReply;
import com.example.grpc.server.grpcserver.MultiplyRequest;
import com.example.grpc.server.grpcserver.MultiplyReply;
import com.example.grpc.server.grpcserver.MatrixServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.*;

@Controller
public class MatrixSystemController {

    private final StorageService matrixSystem;

    int[][] matrix1;
    int[][] matrix2;

    int size1;
    int size2;

    String ip1 = "";
    String ip2 = "";
    String ip3 = "";
    String ip4 = "";
    String ip5 = "";
    String ip6 = "";
    String ip7 = "";
    String ip8 = "";

	@Autowired
	public MatrixSystemController(StorageService matrixSystem) {
		this.matrixSystem = matrixSystem;
	}

	@GetMapping("/")
	public String listUploadedFiles(Model model) throws IOException {
		model.addAttribute("files", matrixSystem.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(MatrixSystemController.class, "serveFile", 
                path.getFileName().toString()).build().toUri().toString()).collect(Collectors.toList()));
		return "upload";
	}

	@PostMapping("/")
	public String fileUpload(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2, RedirectAttributes redirectAttributes) throws IOException {
        String content = new String(file1.getBytes());
        String[] fileMatrix1 = content.split("\\s+");
        size1 = (int) Math.round(Math.sqrt(fileMatrix1.length));
    
        String content1 = new String(file2.getBytes());
        String[] fileMatrix2 = content1.split("\\s+");
        size2 = (int) Math.round(Math.sqrt(fileMatrix2.length));
    
        if(isValidMatrix(fileMatrix1.length) && isValidMatrix(fileMatrix2.length) && size1 == size2){
            int A[][] = new int[size1][size1];
            int B[][] = new int[size1][size1];
    
            for(int i=0; i<size1; i++){
                for(int j=0; j<size1; j++){
                    A[i][j] = Integer.parseInt((fileMatrix1[(i*size1) + j]).trim());
                    B[i][j] = Integer.parseInt((fileMatrix2[(i*size1) + j]).trim());
                }
            }

            matrix1 = A;
            matrix2 = B;
    
            redirectAttributes.addFlashAttribute("message", "Success!");
        }
        else{
             redirectAttributes.addFlashAttribute("message", "Error: One of the matrices has invalid dimensions!");
        }
    
        return "redirect:/";
    }

    @GetMapping("/multiply")
	public String calculate(Model model){
        ManagedChannel channel1 = ManagedChannelBuilder.forAddress(ip1, 9090).usePlaintext().build();
        ManagedChannel channel2 = ManagedChannelBuilder.forAddress(ip2, 9090).usePlaintext().build();
        ManagedChannel channel3 = ManagedChannelBuilder.forAddress(ip3, 9090).usePlaintext().build();
        ManagedChannel channel4 = ManagedChannelBuilder.forAddress(ip4, 9090).usePlaintext().build();
        ManagedChannel channel5 = ManagedChannelBuilder.forAddress(ip5, 9090).usePlaintext().build();
        ManagedChannel channel6 = ManagedChannelBuilder.forAddress(ip6, 9090).usePlaintext().build();
        ManagedChannel channel7 = ManagedChannelBuilder.forAddress(ip7, 9090).usePlaintext().build();
        ManagedChannel channel8 = ManagedChannelBuilder.forAddress(ip8, 9090).usePlaintext().build();

        MatrixServiceGrpc.MatrixServiceBlockingStub stub1 = MatrixServiceGrpc.newBlockingStub(channel1);
        MatrixServiceGrpc.MatrixServiceBlockingStub stub2 = MatrixServiceGrpc.newBlockingStub(channel2);
        MatrixServiceGrpc.MatrixServiceBlockingStub stub3 = MatrixServiceGrpc.newBlockingStub(channel3);
        MatrixServiceGrpc.MatrixServiceBlockingStub stub4 = MatrixServiceGrpc.newBlockingStub(channel4);
        MatrixServiceGrpc.MatrixServiceBlockingStub stub5 = MatrixServiceGrpc.newBlockingStub(channel5);
        MatrixServiceGrpc.MatrixServiceBlockingStub stub6 = MatrixServiceGrpc.newBlockingStub(channel6);
        MatrixServiceGrpc.MatrixServiceBlockingStub stub7 = MatrixServiceGrpc.newBlockingStub(channel7);
        MatrixServiceGrpc.MatrixServiceBlockingStub stub8 = MatrixServiceGrpc.newBlockingStub(channel8);

        int[][] ans = multiplyMatrixBlock(matrix1, matrix2, stub1, stub2, stub3, stub4, stub5, stub6, stub7, stub8);

        channel1.shutdown();
        channel2.shutdown();
        channel3.shutdown();
        channel4.shutdown();
        channel5.shutdown();
        channel6.shutdown();
        channel7.shutdown();
        channel8.shutdown();

        model.addAttribute("ans", matToStr(ans));
        return "multiply";
	}

    @GetMapping("/multiplydl")
	public String dlCalculate(Model model, @RequestParam(value="deadline", defaultValue="") String deadline){
        ManagedChannel channel1 = ManagedChannelBuilder.forAddress(ip1, 9090).usePlaintext().build();
        ManagedChannel channel2 = ManagedChannelBuilder.forAddress(ip2, 9090).usePlaintext().build();
        ManagedChannel channel3 = ManagedChannelBuilder.forAddress(ip3, 9090).usePlaintext().build();
        ManagedChannel channel4 = ManagedChannelBuilder.forAddress(ip4, 9090).usePlaintext().build();
        ManagedChannel channel5 = ManagedChannelBuilder.forAddress(ip5, 9090).usePlaintext().build();
        ManagedChannel channel6 = ManagedChannelBuilder.forAddress(ip6, 9090).usePlaintext().build();
        ManagedChannel channel7 = ManagedChannelBuilder.forAddress(ip7, 9090).usePlaintext().build();
        ManagedChannel channel8 = ManagedChannelBuilder.forAddress(ip8, 9090).usePlaintext().build();

        MatrixServiceGrpc.MatrixServiceBlockingStub stub1 = MatrixServiceGrpc.newBlockingStub(channel1);
        MatrixServiceGrpc.MatrixServiceBlockingStub stub2 = MatrixServiceGrpc.newBlockingStub(channel2);
        MatrixServiceGrpc.MatrixServiceBlockingStub stub3 = MatrixServiceGrpc.newBlockingStub(channel3);
        MatrixServiceGrpc.MatrixServiceBlockingStub stub4 = MatrixServiceGrpc.newBlockingStub(channel4);
        MatrixServiceGrpc.MatrixServiceBlockingStub stub5 = MatrixServiceGrpc.newBlockingStub(channel5);
        MatrixServiceGrpc.MatrixServiceBlockingStub stub6 = MatrixServiceGrpc.newBlockingStub(channel6);
        MatrixServiceGrpc.MatrixServiceBlockingStub stub7 = MatrixServiceGrpc.newBlockingStub(channel7);
        MatrixServiceGrpc.MatrixServiceBlockingStub stub8 = MatrixServiceGrpc.newBlockingStub(channel8);

        long dl = Long.parseLong(deadline);

        int[][] ans = multiplyMatrixBlockDeadline(matrix1, matrix2, dl, stub1, stub2, stub3, stub4, stub5, stub6, stub7, stub8);

        channel1.shutdown();
        channel2.shutdown();
        channel3.shutdown();
        channel4.shutdown();
        channel5.shutdown();
        channel6.shutdown();
        channel7.shutdown();
        channel8.shutdown();

        if(ans.length==0){
            model.addAttribute("error", "Deadline too short");
        }
        else{
            model.addAttribute("ans", matToStr(ans));
        }
        return "multiply";
	}

    // multiply matrix blocks
    public static int[][] multiplyMatrixBlock(int A[][], int B[][],
        MatrixServiceGrpc.MatrixServiceBlockingStub stub1,
        MatrixServiceGrpc.MatrixServiceBlockingStub stub2,
        MatrixServiceGrpc.MatrixServiceBlockingStub stub3,
        MatrixServiceGrpc.MatrixServiceBlockingStub stub4,
        MatrixServiceGrpc.MatrixServiceBlockingStub stub5,
        MatrixServiceGrpc.MatrixServiceBlockingStub stub6,
        MatrixServiceGrpc.MatrixServiceBlockingStub stub7,
        MatrixServiceGrpc.MatrixServiceBlockingStub stub8
    ){
        int MAX = A.length;
        if(MAX == 2){
            List<Integer> C = stub1.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(A)).addAllB(matToArr(B)).build()).getCList();
            return arrToMat(C);
        }
        else{
            int size= MAX/2;
            ArrayList<int[][]> ab = split(A);
            ArrayList<int[][]> bb = split(B);
            
            List<Integer> part1 = stub1.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(0))).addAllB(matToArr(bb.get(0))).build()).getCList();
            List<Integer> part2 = stub2.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(1))).addAllB(matToArr(bb.get(2))).build()).getCList();
            List<Integer> part3 = stub3.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(0))).addAllB(matToArr(bb.get(1))).build()).getCList();
            List<Integer> part4 = stub4.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(1))).addAllB(matToArr(bb.get(3))).build()).getCList();
            List<Integer> part5 = stub5.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(2))).addAllB(matToArr(bb.get(0))).build()).getCList();
            List<Integer> part6 = stub6.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(3))).addAllB(matToArr(bb.get(2))).build()).getCList();
            List<Integer> part7 = stub7.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(2))).addAllB(matToArr(bb.get(1))).build()).getCList();
            List<Integer> part8 = stub8.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(3))).addAllB(matToArr(bb.get(3))).build()).getCList();

            int[][] A3  = arrToMat(stub1.addBlock(AddRequest.newBuilder().addAllA(part1).addAllB(part2).build()).getCList());
            int[][] B3 = arrToMat(stub1.addBlock(AddRequest.newBuilder().addAllA(part3).addAllB(part4).build()).getCList());
            int[][] C3 = arrToMat(stub1.addBlock(AddRequest.newBuilder().addAllA(part5).addAllB(part6).build()).getCList());
            int[][] D3 = arrToMat(stub1.addBlock(AddRequest.newBuilder().addAllA(part7).addAllB(part8).build()).getCList());
            return calc(A3, B3, C3, D3, MAX, size);
        }
    }

    // multiply matrix with deadline
    public static int[][] multiplyMatrixBlockDeadline(int A[][], int B[][], long deadline,
        MatrixServiceGrpc.MatrixServiceBlockingStub stub1,
        MatrixServiceGrpc.MatrixServiceBlockingStub stub2,
        MatrixServiceGrpc.MatrixServiceBlockingStub stub3,
        MatrixServiceGrpc.MatrixServiceBlockingStub stub4,
        MatrixServiceGrpc.MatrixServiceBlockingStub stub5,
        MatrixServiceGrpc.MatrixServiceBlockingStub stub6,
        MatrixServiceGrpc.MatrixServiceBlockingStub stub7,
        MatrixServiceGrpc.MatrixServiceBlockingStub stub8
    ){
        int MAX = A.length;
        if(MAX == 2){
            List<Integer> C = stub1.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(A)).addAllB(matToArr(B)).build()).getCList();
            return arrToMat(C);
        }
        else{
            int size= MAX/2;
            int blocks = 8;
            ArrayList<int[][]> ab = split(A);
            ArrayList<int[][]> bb = split(B);
            List<Integer> part2 = new ArrayList<Integer>();
            List<Integer> part3 = new ArrayList<Integer>();
            List<Integer> part4 = new ArrayList<Integer>();
            List<Integer> part5 = new ArrayList<Integer>();
            List<Integer> part6 = new ArrayList<Integer>();
            List<Integer> part7 = new ArrayList<Integer>();
            List<Integer> part8 = new ArrayList<Integer>();
            long start = System.nanoTime();
            List<Integer> part1 = stub1.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(0))).addAllB(matToArr(bb.get(0))).build()).getCList();
            long end = System.nanoTime();
            long time = end-start;
            int servers = (int) Math.round((time*blocks)/deadline);
            switch(servers){
                case 1:
                    part2 = stub1.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(1))).addAllB(matToArr(bb.get(2))).build()).getCList();
                    part3 = stub1.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(0))).addAllB(matToArr(bb.get(1))).build()).getCList();
                    part4 = stub1.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(1))).addAllB(matToArr(bb.get(3))).build()).getCList();
                    part5 = stub1.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(2))).addAllB(matToArr(bb.get(0))).build()).getCList();
                    part6 = stub1.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(3))).addAllB(matToArr(bb.get(2))).build()).getCList();
                    part7 = stub1.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(2))).addAllB(matToArr(bb.get(1))).build()).getCList();
                    part8 = stub1.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(3))).addAllB(matToArr(bb.get(3))).build()).getCList();
                    break;
                case 2:
                    part2 = stub2.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(1))).addAllB(matToArr(bb.get(2))).build()).getCList();
                    part3 = stub1.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(0))).addAllB(matToArr(bb.get(1))).build()).getCList();
                    part4 = stub2.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(1))).addAllB(matToArr(bb.get(3))).build()).getCList();
                    part5 = stub1.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(2))).addAllB(matToArr(bb.get(0))).build()).getCList();
                    part6 = stub2.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(3))).addAllB(matToArr(bb.get(2))).build()).getCList();
                    part7 = stub1.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(2))).addAllB(matToArr(bb.get(1))).build()).getCList();
                    part8 = stub2.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(3))).addAllB(matToArr(bb.get(3))).build()).getCList();
                    break;
                case 3:
                    part2 = stub2.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(1))).addAllB(matToArr(bb.get(2))).build()).getCList();
                    part3 = stub3.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(0))).addAllB(matToArr(bb.get(1))).build()).getCList();
                    part4 = stub1.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(1))).addAllB(matToArr(bb.get(3))).build()).getCList();
                    part5 = stub2.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(2))).addAllB(matToArr(bb.get(0))).build()).getCList();
                    part6 = stub3.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(3))).addAllB(matToArr(bb.get(2))).build()).getCList();
                    part7 = stub1.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(2))).addAllB(matToArr(bb.get(1))).build()).getCList();
                    part8 = stub2.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(3))).addAllB(matToArr(bb.get(3))).build()).getCList();
                    break;
                case 4:
                    part2 = stub2.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(1))).addAllB(matToArr(bb.get(2))).build()).getCList();
                    part3 = stub3.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(0))).addAllB(matToArr(bb.get(1))).build()).getCList();
                    part4 = stub4.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(1))).addAllB(matToArr(bb.get(3))).build()).getCList();
                    part5 = stub1.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(2))).addAllB(matToArr(bb.get(0))).build()).getCList();
                    part6 = stub2.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(3))).addAllB(matToArr(bb.get(2))).build()).getCList();
                    part7 = stub3.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(2))).addAllB(matToArr(bb.get(1))).build()).getCList();
                    part8 = stub4.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(3))).addAllB(matToArr(bb.get(3))).build()).getCList();
                    break;
                case 5:
                    part2 = stub2.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(1))).addAllB(matToArr(bb.get(2))).build()).getCList();
                    part3 = stub3.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(0))).addAllB(matToArr(bb.get(1))).build()).getCList();
                    part4 = stub4.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(1))).addAllB(matToArr(bb.get(3))).build()).getCList();
                    part5 = stub5.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(2))).addAllB(matToArr(bb.get(0))).build()).getCList();
                    part6 = stub1.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(3))).addAllB(matToArr(bb.get(2))).build()).getCList();
                    part7 = stub2.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(2))).addAllB(matToArr(bb.get(1))).build()).getCList();
                    part8 = stub3.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(3))).addAllB(matToArr(bb.get(3))).build()).getCList();
                    break;
                case 6:
                    part2 = stub2.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(1))).addAllB(matToArr(bb.get(2))).build()).getCList();
                    part3 = stub3.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(0))).addAllB(matToArr(bb.get(1))).build()).getCList();
                    part4 = stub4.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(1))).addAllB(matToArr(bb.get(3))).build()).getCList();
                    part5 = stub5.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(2))).addAllB(matToArr(bb.get(0))).build()).getCList();
                    part6 = stub6.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(3))).addAllB(matToArr(bb.get(2))).build()).getCList();
                    part7 = stub1.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(2))).addAllB(matToArr(bb.get(1))).build()).getCList();
                    part8 = stub2.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(3))).addAllB(matToArr(bb.get(3))).build()).getCList();
                    break;
                case 7:
                    part2 = stub2.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(1))).addAllB(matToArr(bb.get(2))).build()).getCList();
                    part3 = stub3.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(0))).addAllB(matToArr(bb.get(1))).build()).getCList();
                    part4 = stub4.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(1))).addAllB(matToArr(bb.get(3))).build()).getCList();
                    part5 = stub5.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(2))).addAllB(matToArr(bb.get(0))).build()).getCList();
                    part6 = stub6.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(3))).addAllB(matToArr(bb.get(2))).build()).getCList();
                    part7 = stub7.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(2))).addAllB(matToArr(bb.get(1))).build()).getCList();
                    part8 = stub1.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(3))).addAllB(matToArr(bb.get(3))).build()).getCList();
                    break;
                case 8:
                    part2 = stub2.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(1))).addAllB(matToArr(bb.get(2))).build()).getCList();
                    part3 = stub3.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(0))).addAllB(matToArr(bb.get(1))).build()).getCList();
                    part4 = stub4.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(1))).addAllB(matToArr(bb.get(3))).build()).getCList();
                    part5 = stub5.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(2))).addAllB(matToArr(bb.get(0))).build()).getCList();
                    part6 = stub6.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(3))).addAllB(matToArr(bb.get(2))).build()).getCList();
                    part7 = stub7.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(2))).addAllB(matToArr(bb.get(1))).build()).getCList();
                    part8 = stub8.multiplyBlock(MultiplyRequest.newBuilder().addAllA(matToArr(ab.get(3))).addAllB(matToArr(bb.get(3))).build()).getCList();
                    break;
                default:
                    System.out.println("Too few servers");
                    return new int[0][0];
            }

            int[][] A3 = arrToMat(stub1.addBlock(AddRequest.newBuilder().addAllA(part1).addAllB(part2).build()).getCList());
            int[][] B3 = arrToMat(stub1.addBlock(AddRequest.newBuilder().addAllA(part3).addAllB(part4).build()).getCList());
            int[][] C3 = arrToMat(stub1.addBlock(AddRequest.newBuilder().addAllA(part5).addAllB(part6).build()).getCList());
            int[][] D3 = arrToMat(stub1.addBlock(AddRequest.newBuilder().addAllA(part7).addAllB(part8).build()).getCList());

            return calc(A3, B3, C3, D3, MAX, size);
        }
    }

    //check if matrix has valid size
    public static Boolean isValidMatrix(int size){
        if(size != 0 && (size & (size - 1)) == 0){
            return true;
        }
        else{
            return false;
        }
    }

    // split matrix to blocks
    public static ArrayList<int[][]> split(int M[][]){
        int size = M[0].length;
        int dim = size/2;
        ArrayList<int[][]> A = new ArrayList<int[][]>();

        for(int i = 0; i < M[0].length; i=i+dim){
            for(int j = 0; j < M[0].length; j=j+dim){

                int[][] B = new int[dim][dim];
                for(int k=0; k < dim; k++){
                    for(int l=0; l < dim; l++){
                        B[l][k] = M[l+i][j+k];
                    }
                }
                A.add(B);
            }
        }
        return A;
    }

    //calculate answer from blocks
    public static int[][] calc(int[][] A, int[][] B, int[][] C, int[][] D, int MAX, int size){
        int[][] res= new int[MAX][MAX];

        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                res[i][j]=A[i][j];
            }
        }

        for (int i = 0; i < size; i++){
            for (int j = size; j < MAX; j++){
                res[i][j]=B[i][j-size];
            }
        }

        for (int i = size; i < MAX; i++){
            for (int j = 0; j < size; j++){
                res[i][j]=C[i-size][j];
            }
        }

        for (int i = size; i < MAX; i++){
            for (int j = size; j < MAX; j++){
                res[i][j]=D[i-size][j-size];
            }
        }

        return res;
    }

    // convert matrix to array
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

    // convert array to matrix
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

    // convert matrix to string
    public String[] matToStr(int[][] ans){
        String[] answer = new String[size1];
        String line = "";
        for (int i=0; i<size1; i++){
            for (int j=0; j<size1;j++){
                line = line + Integer.toString(ans[i][j]) + " ";
            }
            answer[i] = line;
            line = "";
        }
        return answer;
    }
}

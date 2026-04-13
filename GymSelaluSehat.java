
package UTPPEMLAN1;

import java.util.*;

abstract class Member{
    private String id;
    private String nama;
    private int saldo;
    
    public Member (String id, String saldo){
        this.id = id;
        this.nama = nama;
        this.saldo = 0;
    }
    
    public String getId(){
        return id;
    }
    
    public String getNama(){
        return nama;
    }
    
    public int getSaldo(){
        return saldo;
    }
    
    public void TopUp(int jumlah){
        saldo += jumlah;
    }
    
    public boolean kurangSaldo(int jumlah){
        if (saldo >= jumlah){
            saldo -= jumlah;
            return true;
        }
        return false;
    }
    
    public abstract int Pembayaran(int harga, int sesi);
    
    public abstract String getType();
}



class Reguler extends Member{
    public Reguler(String id, String nama){
        super (id, nama);
    }
    
    @Override
    public int Pembayaran(int harga, int sesi){
        int hargaAwal = harga* sesi;
        
        
        int discount = 0;
        if (sesi > 5){
            discount += (hargaAwal *10)/100;
        }
        
        int subTotal = hargaAwal - discount;
        int pajak = (subTotal * 5)/100;
        int totalBiaya = subTotal + pajak;
        
        
        return Math.max(totalBiaya, 0);
    }
    
    @Override 
    public String getType(){
        return "REGULER";
    }
}



class VIP extends Member{
    public VIP (String id, String nama){
        super (id, nama);
    }
    
    
    @Override 
    public int Pembayaran(int harga, int sesi){
        int hargaAwal = harga * sesi;
        
        int discount = 0;
        if (sesi > 5){
            discount += (hargaAwal *10)/100;
        }
        
        discount += (hargaAwal * 15)/100;
        
        int subTotal = hargaAwal - discount;
        int pajak = (subTotal * 5)/100;
        int totalBiaya = subTotal + pajak;
        
        return Math.max(totalBiaya, 0);
    }
    
    @Override
    public String getType(){
        return "VIP";
    }
}


public class GymSelaluSehat {
    static Map<String,Member> members = new HashMap <>();
    
    public static int getHarga(String layanan){
        switch(layanan){
            case "cardio":
                return 20000;
            case "yoga":
                return 25000;
            case "personal_training":
                return 40000;
            default :
                return -1;
        }
    }
    
    public static void main(String [] args){
        Scanner input = new Scanner(System.in);
        
        int N = input.nextInt();
        
        for(int i = 0; i<N; i++){
            String kondisi = input.next();
            
            switch (kondisi){
                case "ADD":
                    String tipe = input.next();
                    String id = input.next();
                    String nama = input.next();
                    
                    if(members.containsKey(id)){
                        System.out.println("Member sudah terdaftar");
                    }else{
                        if (tipe.equals("REGULER")){
                            members.put(id, new Reguler(id, nama));
                        }else if (tipe.equals("VIP")){
                            members.put(id, new VIP(id, nama));
                        }
                        
                        System.out.println(tipe + " " + id + " berhasil ditambahkan");
                    }
                    break;
                    
                case "TOPUP": 
                    id = input.next();
                    int jumlah = input.nextInt();
                    
                    if (!members.containsKey(id)){
                        System.out.println("Member tidak ditemukan");
                    }else{
                        Member m = members.get(id);
                        m.TopUp(jumlah);
                        System.out.println("Saldo " + id + ": " + m.getSaldo());
                    }
                    break;
                    
                case "BUY":
                    id = input.next();
                    String layanan = input.next();
                    int sesi = input.nextInt();
                    
                    if (!members.containsKey(id)){
                        System.out.println("Member tidak ditemukan");
                        break;
                    }
                    
                    int harga = getHarga(layanan);
                    if (harga == -1){
                        System.out.println("Layanan tidak valid");
                        break;
                    }
                    
                    Member m = members.get(id);
                    int totalBiaya = m.Pembayaran(harga, sesi);
                    
                    if (m.getSaldo()< totalBiaya){
                        System.out.println("Saldo " + id + " tidak cukup");
                    }else{
                        m.kurangSaldo(totalBiaya);
                        System.out.println("Total bayar " + id + ": " + totalBiaya);
                        System.out.println("Saldo " + id + ": " + m.getSaldo());
                    }
                    break;
                    
                case "CHECK":
                    id = input.next();
                    
                    if(!members.containsKey(id)){
                        System.out.println("Member tidak ditemukan");
                    }else{
                        Member n = members.get(id);
                        System.out.println(
                                n.getId()+ " | " +
                                n.getNama()+ " | " +
                                n.getType()+ " | saldo: " +
                                n.getSaldo()
                        );
                    }
                    break;
                    
                case "COUNT":
                    System.out.println("Total member: " + members.size());
                    break;
            }
        }
        input.close();
    }
}

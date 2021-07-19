package com.example.user.crudpengguna;

public class ModelPengguna {
    //generate > getter and setter > pilih semuanya

    public String getId_pengguna() {
        return id_pengguna;
    }

    public void setId_pengguna(String id_pengguna) {
        this.id_pengguna = id_pengguna;
    }

    public String getNama_pengguna() {
        return nama_pengguna;
    }

    public void setNama_pengguna(String nama_pengguna) {
        this.nama_pengguna = nama_pengguna;
    }

    public String getAlamat_pengguna() {
        return alamat_pengguna;
    }

    public void setAlamat_pengguna(String alamat_pengguna) {
        this.alamat_pengguna = alamat_pengguna;
    }

    public String getHp_pengguna() {
        return hp_pengguna;
    }

    public void setHp_pengguna(String hp_pengguna) {
        this.hp_pengguna = hp_pengguna;
    }

    String id_pengguna, nama_pengguna, alamat_pengguna, hp_pengguna;
}

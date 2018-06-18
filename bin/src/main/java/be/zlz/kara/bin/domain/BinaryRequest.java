package be.zlz.kara.bin.domain;

import javax.persistence.*;

@Entity
public class BinaryRequest {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    @ManyToOne
    private Bin bin;

    @Lob
    private byte[] binaryRequest;

    public long getId() {
        return id;
    }

    public void setBinaryRequest(byte[] binaryRequest) {
        this.binaryRequest = binaryRequest;
    }

    public byte[] getBinaryRequest() {
        return binaryRequest;
    }

    public Bin getBin() {
        return bin;
    }

    public void setBin(Bin bin) {
        this.bin = bin;
    }
}

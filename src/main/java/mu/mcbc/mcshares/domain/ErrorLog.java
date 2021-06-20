package mu.mcbc.mcshares.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * A Shares.
 */
@Entity
@Table(name = "errorlog")
public class ErrorLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "msg")
    private String msg;

    @Column(name = "tsp")
    private Instant tsp;

    public ErrorLog() {
        // TODO Auto-generated constructor stub
    }

    public ErrorLog(Long id, String msg, Instant tsp) {
        super();
        this.id = id;
        this.msg = msg;
        this.tsp = tsp;
    }

    public ErrorLog(String msg, Instant tsp) {
        this.msg = msg;
        this.tsp = tsp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Instant getDob() {
        return tsp;
    }

    public void setDob(Instant dob) {
        this.tsp = dob;
    }
}

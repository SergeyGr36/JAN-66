package com.ra.course.janus.faculty.entity;

public class Mark {

    private Integer tid;

    private Integer score;
    private String  reference;

    public Integer getTid() { return tid; }

    public void setTid(Integer tid) {this.tid = tid;  }

    public Integer getScore() { return score;   }

    public void setScore(Integer score) {this.score = score; }

    public String getReference() { return reference; }

    public void setReference(String reference) {this.reference = reference;  }

    @Override
    public String toString() {
        return "Mark{" +
                "score=" + score +
                ", reference='" + reference + '\'' +
                '}';
    }

}

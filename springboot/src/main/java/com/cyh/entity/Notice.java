package com.cyh.entity;

/**
 * 公告
*/
public class Notice  {

    /** ID */
    private Integer id;
    /** 用户名 */
    private String title;
    /** 密码 */
    private String content;
    /** 姓名 */
    private String time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
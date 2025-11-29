package org.example.ticketmanagement.pojo;

import lombok.Data;

@Data
public class Result {
        private Integer code; //编码：1成功，0为失败
        private String msg; //错误信息
        private Object data; //数据

    // 添加默认构造函数
    public Result() {
    }

    // 添加带参数的构造函数
    public Result(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

        public static Result success() {
            Result result = new Result();
            result.code = 1;
            result.msg = "success";
            return result;
        }

        public static Result success(Object object) {
            Result result = new Result();
            result.data = object;
            result.code = 1;
            result.msg = "success";
            return result;
        }

        public static Result error(String msg) {
            Result result = new Result();
            result.msg = msg;
            result.code = 0;
            return result;
        }
        public static Result success(String message, Object data) {
            return new Result(1, message, data);
    }

}


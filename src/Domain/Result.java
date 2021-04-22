package Domain;

public class Result {
    private boolean result;
    private Object data;

    public Result(boolean result, Object data){
        this.result=result;
        this.data=data;
    }

    public Object getdata() {
        return data;
    }

    public boolean isResult() {
        return result;
    }
}

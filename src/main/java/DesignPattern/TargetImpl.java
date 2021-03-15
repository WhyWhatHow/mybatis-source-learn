package DesignPattern;

public class TargetImpl implements Target {
    @Override
    public String execute(String sql) {
        System.out.println("execute() " + sql);
        return sql;
    }
}

package pl.mkotra.spring.model;

public record Item(String id, String name) {

    public String simpleName() {
        return id + name;
    }
}

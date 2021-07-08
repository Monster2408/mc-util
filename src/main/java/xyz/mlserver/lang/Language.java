package xyz.mlserver.lang;

public enum Language {
    ENGLISH("ENGLISH"),
    JAPANESE("日本語") {
        @Override
        public String toString() {
            return name().toUpperCase();
        }
    };
    private String name;
    Language(String name) { this.name = name; }

    public String getName() { return name; }
}
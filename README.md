Реализовать класс похожий на java.util.stream.Stream 
---
(http://www.oracle.com/technetwork/articles/java/ma14-java-se-8-streams-2177646.html)

Использование этого класса должно выглядеть примерно так:

    List<Person> someCollection = ...

    Map m = Streams.of(someCollection)
                    .filter(p -> p.getAge() > 20)
                    .transform( p -> new Person(p.geAge() + 30)))
                    .toMap(p -> p.geName(), p -> p);

Streams.of() - статический метод, который принимает коллекцию и создает новый объект Streams

filter() - оставляет в коллекции только те элементы, которые удовлетворяют условию в лямбде.

transform() - преобразует элемент в другой.

toMap - принимает 2 лямбды для создания мапы, в одной указывается, что использовать в качестве ключа, в другой, что в качестве значения.

После выполнения всех операций коллекция someCollection не должна поменяться.

Класс надо параметризовать используя правило PECS


    public class Streams<T> {
       public static Streams of(List list) {
                ....
        }
    
        public Streams filter(........) {
               ...
             return this;
        }
    
         public Streams transform(........) {
               ...
             return this;
        }
    
         public Map toMap(........) {
               ...  
        }
    }

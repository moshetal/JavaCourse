import java.lang.reflect.*;
import java.util.*;

public class Investigator implements reflection.api.Investigator {
    
    private Object instance;
    private Class<?> clazz;
    
    // Default public constructor as required
    public Investigator() {
    }
    
    @Override
    public void load(Object anInstanceOfSomething) {
        this.instance = anInstanceOfSomething;
        this.clazz = anInstanceOfSomething.getClass();
    }
    
    @Override
    public int getTotalNumberOfMethods() {
        try {
            return clazz.getDeclaredMethods().length;
        } catch (Exception e) {
            return 0;
        }
    }
    
    @Override
    public int getTotalNumberOfConstructors() {
        try {
            return clazz.getDeclaredConstructors().length;
        } catch (Exception e) {
            return 0;
        }
    }
    
    @Override
    public int getTotalNumberOfFields() {
        try {
            return clazz.getDeclaredFields().length;
        } catch (Exception e) {
            return 0;
        }
    }
    
    @Override
    public Set<String> getAllImplementedInterfaces() {
        try {
            Set<String> interfaces = new HashSet<>();
            Class<?>[] implementedInterfaces = clazz.getInterfaces();
            for (Class<?> iface : implementedInterfaces) {
                interfaces.add(iface.getSimpleName());
            }
            return interfaces;
        } catch (Exception e) {
            return new HashSet<>();
        }
    }
    
    @Override
    public int getCountOfConstantFields() {
        try {
            int count = 0;
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (Modifier.isFinal(field.getModifiers())) {
                    count++;
                }
            }
            return count;
        } catch (Exception e) {
            return 0;
        }
    }
    
    @Override
    public int getCountOfStaticMethods() {
        try {
            int count = 0;
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (Modifier.isStatic(method.getModifiers())) {
                    count++;
                }
            }
            return count;
        } catch (Exception e) {
            return 0;
        }
    }
    
    @Override
    public boolean isExtending() {
        try {
            Class<?> superClass = clazz.getSuperclass();
            return superClass != null && !superClass.equals(Object.class);
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public String getParentClassSimpleName() {
        try {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && !superClass.equals(Object.class)) {
                return superClass.getSimpleName();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public boolean isParentClassAbstract() {
        try {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && !superClass.equals(Object.class)) {
                return Modifier.isAbstract(superClass.getModifiers());
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public Set<String> getNamesOfAllFieldsIncludingInheritanceChain() {
        try {
            Set<String> fieldNames = new HashSet<>();
            Class<?> currentClass = clazz;
            
            while (currentClass != null) {
                Field[] fields = currentClass.getDeclaredFields();
                for (Field field : fields) {
                    fieldNames.add(field.getName());
                }
                currentClass = currentClass.getSuperclass();
            }
            
            return fieldNames;
        } catch (Exception e) {
            return new HashSet<>();
        }
    }
    
    @Override
    public int invokeMethodThatReturnsInt(String methodName, Object... args) {
        try {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName) && method.getReturnType() == int.class) {
                    method.setAccessible(true);
                    return (Integer) method.invoke(instance, args);
                }
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }
    
    @Override
    public Object createInstance(int numberOfArgs, Object... args) {
        try {
            Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            for (Constructor<?> constructor : constructors) {
                if (constructor.getParameterCount() == numberOfArgs) {
                    constructor.setAccessible(true);
                    return constructor.newInstance(args);
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public Object elevateMethodAndInvoke(String name, Class<?>[] parametersTypes, Object... args) {
        try {
            Method method = clazz.getDeclaredMethod(name, parametersTypes);
            method.setAccessible(true);
            return method.invoke(instance, args);
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public String getInheritanceChain(String delimiter) {
        try {
            List<String> chain = new ArrayList<>();
            Class<?> currentClass = clazz;
            
            while (currentClass != null) {
                chain.add(0, currentClass.getSimpleName());
                currentClass = currentClass.getSuperclass();
            }
            
            return String.join(delimiter, chain);
        } catch (Exception e) {
            return "";
        }
    }
} 
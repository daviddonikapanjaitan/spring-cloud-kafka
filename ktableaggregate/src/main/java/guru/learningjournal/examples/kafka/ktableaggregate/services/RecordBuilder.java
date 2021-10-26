package guru.learningjournal.examples.kafka.ktableaggregate.services;

import guru.learningjournal.examples.kafka.model.DepartmentAggregate;
import guru.learningjournal.examples.kafka.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RecordBuilder {

    Logger log = LoggerFactory.getLogger(RecordBuilder.class);

    public DepartmentAggregate init(){
        DepartmentAggregate departmentAggregate = new DepartmentAggregate();
        departmentAggregate.setEmployeeCount(0);
        departmentAggregate.setTotalSalary(0);
        departmentAggregate.setAvgSalary(0D);
        return departmentAggregate;
    }

    public DepartmentAggregate aggregate(Employee emp, DepartmentAggregate aggValue){
        DepartmentAggregate departmentAggregate = new DepartmentAggregate();
        departmentAggregate.setEmployeeCount(aggValue.getEmployeeCount() + 1);
        departmentAggregate.setTotalSalary(aggValue.getTotalSalary() + emp.getSalary());
        departmentAggregate.setAvgSalary((aggValue.getTotalSalary() + emp.getSalary()) / (aggValue.getEmployeeCount() + 1D));
        return departmentAggregate;
    }

    public DepartmentAggregate subtract(Employee emp, DepartmentAggregate aggValue){
        DepartmentAggregate departmentAggregate = new DepartmentAggregate();
        departmentAggregate.setEmployeeCount(aggValue.getEmployeeCount() - 1);
        departmentAggregate.setTotalSalary(aggValue.getTotalSalary() - emp.getSalary());
        departmentAggregate.setAvgSalary((aggValue.getTotalSalary() - emp.getSalary()) / (aggValue.getEmployeeCount() - 1D));
        return departmentAggregate;
    }
}

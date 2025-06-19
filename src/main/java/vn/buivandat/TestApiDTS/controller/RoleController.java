package vn.buivandat.TestApiDTS.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.buivandat.TestApiDTS.Service.RoleService;
import vn.buivandat.TestApiDTS.domain.Role;
import vn.buivandat.TestApiDTS.util.annotation.ApiMessage;
import vn.buivandat.TestApiDTS.util.error.IdInvalidException;

@RestController
public class RoleController {
    private RoleService roleService;
    public RoleController(RoleService roleService){
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    @ApiMessage("create roles success")
    public ResponseEntity<Role> createNewRole(@Valid @RequestBody Role role)
            throws IdInvalidException {
        if(this.roleService.existByName(role.getName())){
                    throw new IdInvalidException("Role với name = "+role.getName()+" đã tồn tại");
                }
                
        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.create(role));
    }

      @PutMapping("/roles")
    @ApiMessage("update roles success")
    public ResponseEntity<Role> updateRole(@RequestBody Role r) throws IdInvalidException
    {   
        boolean existRoleId = this.roleService.existById(r.getId());
        if(existRoleId != true){
            throw new IdInvalidException("Role với id = "+r.getId()+" không tồn tại");
        }

        // if(this.roleService.existByName(r.getName())){
        //     throw new IdInvalidException("Role với name = "+r.getName()+" đã tồn tại");
        // }
       
        return ResponseEntity.ok().body(this.roleService.Update(r));
    }

       @DeleteMapping("/roles/{id}")
    @ApiMessage("delete roles success")
    public ResponseEntity<Void> deleteRoles(@PathVariable("id") long id) throws IdInvalidException {
        boolean existById = this.roleService.existById(id);
        if (existById != true) {
            throw new IdInvalidException("Id " + id + " không tồn tại");
        }
        this.roleService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}

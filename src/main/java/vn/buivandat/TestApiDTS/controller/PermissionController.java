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
import vn.buivandat.TestApiDTS.Service.PermissionService;
import vn.buivandat.TestApiDTS.domain.Permission;
import vn.buivandat.TestApiDTS.util.annotation.ApiMessage;
import vn.buivandat.TestApiDTS.util.error.IdInvalidException;

@RestController
public class PermissionController {
    private PermissionService permissionService;

    public PermissionController(PermissionService permissionService){
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    @ApiMessage("create permission success")
    public ResponseEntity<Permission> createNewPermission(@Valid @RequestBody Permission permission)
            throws IdInvalidException {
        if(this.permissionService.isPermissionExist(permission)){
                    throw new IdInvalidException("Permission đã tồn tại");
                }
        Permission currentPermission = this.permissionService.create(permission);
        return ResponseEntity.status(HttpStatus.CREATED).body(currentPermission);
    }

    @PutMapping("/permissions")
    @ApiMessage("update permissions success")
    public ResponseEntity<Permission> updatePermission(@RequestBody Permission p) throws IdInvalidException
    {   
        boolean existPermissionById = this.permissionService.existById(p.getId());
        if(existPermissionById != true){
            throw new IdInvalidException("Permission với id = "+p.getId()+" không tồn tại");
        }
        //check exist by module, api path. method
        if(this.permissionService.isPermissionExist(p)){
            //check name
            if(this.permissionService.isSameName(p)){
            throw new IdInvalidException("Permission đã tồn tại");
            }
        }
        return ResponseEntity.ok().body(this.permissionService.Update(p));
    }

     @DeleteMapping("/permissions/{id}")
    @ApiMessage("delete permissions success")
    public ResponseEntity<Void> deletePermissions(@PathVariable("id") long id) throws IdInvalidException {
        boolean existById = this.permissionService.existById(id);
        if (existById != true) {
            throw new IdInvalidException("Id " + id + " không tồn tại");
        }
        this.permissionService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}

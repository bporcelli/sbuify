import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { ContextMenuModule } from 'ngx-contextmenu';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AdminRoutingModule } from "./admin-routing.module";
import { AdminHomeComponent } from "./admin-home.component";
import { AuthModule } from "../auth/auth.module";
import { UserListComponent } from "./user-list.component";
import { SharedModule } from "../shared/shared.module";
import { EditUserModalComponent } from "./edit-user-modal.component";
import { CreateUserComponent } from "./create-user.component";
import { RoyaltiesComponent } from "app/admin/royalties.component";
import { RoyaltiesService } from "./royalties.service";
import { ToStatusStringPipe } from "./to-status-string.pipe";
import { RoyaltyModalComponent } from "./royalty-modal.component";

@NgModule({
  imports: [
    CommonModule,
    AdminRoutingModule,
    AuthModule,
    SharedModule,
    InfiniteScrollModule,
    ContextMenuModule,
    NgbModule,
    FormsModule
  ],
  declarations: [
    AdminHomeComponent,
    UserListComponent,
    EditUserModalComponent,
    CreateUserComponent,
    RoyaltiesComponent,
    ToStatusStringPipe,
    RoyaltyModalComponent
  ],
  entryComponents: [
    EditUserModalComponent,
    RoyaltyModalComponent
  ],
  providers: [
    RoyaltiesService
  ]
})
export class AdminModule {}

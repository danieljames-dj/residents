import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AuthGuard } from './auth.guard';
import { HomeComponent } from '../views/home/home.component';
import { LoginComponent } from '../views/login/login.component';
import { CreateComponent } from '../views/create/create.component';
import { GroupHomeComponent } from '../views/group-home/group-home.component';
import { AddHouseComponent } from '../views/add-house/add-house.component';


const routes: Routes = [
    {
        path: 'home',
        component: HomeComponent,
        canActivate: [AuthGuard],
        data: {
            shouldBeSignedIn: true,
            redirect: 'signin'
        }
    },
    {
        path: 'signin',
        component: LoginComponent,
        canActivate: [AuthGuard],
        data: {
            shouldBeSignedIn: false,
            redirect: 'home'
        }
    },
    {
        path: 'create',
        component: CreateComponent,
        canActivate: [AuthGuard],
        data: {
            shouldBeSignedIn: true,
            redirect: 'signin'
        }
    },
    {
        path: 'group/:id',
        component: GroupHomeComponent,
        canActivate: [AuthGuard],
        data: {
            shouldBeSignedIn: true,
            redirect: 'signin'
        }
    },
    {
        path: 'addHouse/:id',
        component: AddHouseComponent,
        canActivate: [AuthGuard],
        data: {
            shouldBeSignedIn: true,
            redirect: 'signin'
        }
    },
    {
        path: '',
        redirectTo: 'signin',
        pathMatch: 'full'
    },
//   ,
//   {
//       path: '',
//       redirectTo: 'info',
//       pathMatch: 'full',
//   },
    {
        path: '**',
        component: LoginComponent//PageNotFoundComponent
    }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule { }

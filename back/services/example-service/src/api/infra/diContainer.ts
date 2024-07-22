import 'reflect-metadata';
import { container } from 'tsyringe';
import { UserService } from '../../services/user/UserService';

container.register('UserService', {
    useClass: UserService
});

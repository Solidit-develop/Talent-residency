import { injectable } from 'tsyringe';
import { getRepository } from 'typeorm';
import { User } from '../../shared/models/UserModel';
import { UserDTO } from '../../shared/dtos/UserDto';

@injectable()
export class UserService {
    public async getUserById(id: number): Promise<User | null > {
        return await getRepository(User).findOneBy({
            id: id
        });
    }

    public async createUser(userDTO: UserDTO): Promise<User> {
        const userRepository = getRepository(User);
        const newUser = userRepository.create(userDTO);
        await userRepository.save(newUser);
        return newUser;
    }
}

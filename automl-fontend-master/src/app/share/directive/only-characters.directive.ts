import {AbstractControl, ValidatorFn} from '@angular/forms';

export function onlyCharacterValidator(nameRe: RegExp): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } | null => {
    let data = control.value;
    if (data) {
      data = data.trim();
    }
    const number = !nameRe.test(data);
    if (!control.value) {
      return null;
    }
    return number ? {'onlyCharacter': {value: control.value}} : null;
  };
}
